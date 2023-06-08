package com.dana.merchantapp.data.home

import com.dana.merchantapp.data.model.Merchant
import com.dana.merchantapp.domain.home.HomeRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) : HomeRepository {
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
                .addOnFailureListener {
                    // Handle the failure case
                    callback(null)
                }
        } else {
            callback(null)
        }
    }
}