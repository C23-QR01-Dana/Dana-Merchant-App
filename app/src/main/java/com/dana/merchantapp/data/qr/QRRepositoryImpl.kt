package com.dana.merchantapp.data.qr

import android.graphics.Bitmap
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.dana.merchantapp.data.home.MerchantMapper
import com.dana.merchantapp.data.model.Merchant
import com.dana.merchantapp.domain.qr.QRRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import javax.inject.Inject


class QRRepositoryImpl @Inject constructor (private val auth: FirebaseAuth, private val firestore: FirebaseFirestore):
    QRRepository {

    override fun generateStaticQR(): Bitmap {
        val content = "DANA#MPM#" + auth.currentUser?.uid
        val qrgEncoder = QRGEncoder(content, null, QRGContents.Type.TEXT, 500)
        qrgEncoder.setColorBlack(0xFFFFFFFF.toInt())
        qrgEncoder.setColorWhite(0xFF000000.toInt())
        return qrgEncoder.bitmap
    }

    override fun generateDynamicQR(amount: String): Bitmap {
        val content = "DANA#MPM#" + auth.currentUser?.uid + "#" + amount
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

    override fun transaction(amount: Int, payerId: String, timestamp: Long, trxType: String, callback: (Boolean) -> Unit) {
        val user = auth.currentUser
        val merchantId = user?.uid

        if (merchantId != null) {
            val db = firestore.collection("transaction").document()
            val transactionData = hashMapOf(
                "amount" to amount,
                "id" to db.id,
                "merchantId" to merchantId,
                "payerId" to payerId,
                "timestamp" to timestamp,
                "trxType" to trxType,
                )
            db.set(transactionData)
                .addOnSuccessListener {
                    Log.v("transaction", "Data successfully saved to Firestore")
                    callback(true)
                }
                .addOnFailureListener { e ->
                    Log.e("transaction", "Error saving data to Firestore", e)
                    callback(false)
                }
        } else {
            Log.e("transaction", "Merchant ID is null")
            callback(false)
        }


    }

}