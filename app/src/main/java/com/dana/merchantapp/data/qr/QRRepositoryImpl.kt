package com.dana.merchantapp.data.qr

import android.graphics.Bitmap
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.dana.merchantapp.presentation.model.Merchant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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

}