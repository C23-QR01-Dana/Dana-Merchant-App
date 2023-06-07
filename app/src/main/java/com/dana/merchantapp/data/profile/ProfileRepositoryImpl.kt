package com.dana.merchantapp.data.profile

import android.net.Uri
import com.dana.merchantapp.data.qr.MerchantMapper
import com.dana.merchantapp.presentation.model.Merchant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileRepositoryImpl: ProfileRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()


    override fun getMerchant(callback: (Merchant?) -> Unit) {
        val merchantId = auth.currentUser?.uid
        if (merchantId != null) {
            val merchantDocRef = firestore.collection("merchant").document(merchantId)
            merchantDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val merchant = MerchantMapper.mapToMerchant(documentSnapshot)
                        callback(merchant)
                    } else {
                        callback(null)
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the failure case
                    callback(null)
                }
        } else {
            callback(null)
        }
    }

    override fun updatePhoto(imageUri: Uri, callback: (Boolean) -> Unit) {
        val merchantId = auth.currentUser?.uid
        val storageRef = storage.reference

        if (merchantId != null) {
            val ref = storageRef.child("merchant/${merchantId}/${merchantId}.jpg")
            ref.putFile(imageUri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        val merchantDocRef = firestore.collection("merchant").document(merchantId)
                        merchantDocRef.update("merchantLogo", uri.toString())
                            .addOnSuccessListener {
                                callback(true)
                            }
                            .addOnFailureListener {
                                callback(false)
                            }
                    }
                }
                .addOnFailureListener {
                    callback(false)
                }
        } else {
            callback(false)
        }
    }

    override fun logoutUser(callback: (Boolean, String) -> Unit) {
        auth.signOut()
        callback(true, "Logout Success")
    }
}