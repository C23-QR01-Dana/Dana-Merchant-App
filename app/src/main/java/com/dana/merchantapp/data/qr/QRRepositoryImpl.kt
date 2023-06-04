package com.dana.merchantapp.data.qr

import android.graphics.Bitmap
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.dana.merchantapp.presentation.model.Merchant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class QRRepositoryImpl: QRRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun generateStaticQR(): Bitmap {
        val content = "DANA#MPM#" + auth.currentUser?.uid
        val qrgEncoder = QRGEncoder(content, null, QRGContents.Type.TEXT, 500)
        qrgEncoder.setColorBlack(0xFFFFFFFF.toInt())
        qrgEncoder.setColorWhite(0xFF000000.toInt())
        return qrgEncoder.bitmap
    }

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

}