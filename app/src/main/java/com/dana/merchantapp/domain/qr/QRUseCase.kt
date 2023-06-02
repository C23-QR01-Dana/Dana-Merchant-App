package com.dana.merchantapp.domain.qr

import android.graphics.Bitmap
import com.dana.merchantapp.data.qr.QRRepository



class QRUseCase (private val qrRepository: QRRepository) {
    fun generateStaticQR() : Bitmap {
        return qrRepository.generateStaticQR()
    }
}