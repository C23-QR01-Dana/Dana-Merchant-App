package com.dana.merchantapp.domain.qr

import android.graphics.Bitmap
import javax.inject.Inject

class GenerateStaticQR @Inject constructor (private val qrRepository: QRRepository) {
    fun generateStaticQR(): Bitmap {
        return qrRepository.generateStaticQR()
    }
}