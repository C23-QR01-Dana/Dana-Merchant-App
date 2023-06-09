package com.dana.merchantapp.domain.qr

import android.graphics.Bitmap
import javax.inject.Inject

class GenerateDynamicQR @Inject constructor (private val qrRepository: QRRepository) {
    fun generateDynamicQR(amount: String) : Bitmap {
        return qrRepository.generateDynamicQR(amount)
    }
}