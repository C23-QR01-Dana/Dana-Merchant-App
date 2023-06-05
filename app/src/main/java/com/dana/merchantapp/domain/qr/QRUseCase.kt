package com.dana.merchantapp.domain.qr

import android.graphics.Bitmap
import com.dana.merchantapp.data.qr.QRRepository
import com.dana.merchantapp.presentation.model.Merchant
import javax.inject.Inject


class QRUseCase  @Inject constructor (private val qrRepository: QRRepository) {
    fun generateStaticQR() : Bitmap {
        return qrRepository.generateStaticQR()
    }

    fun generateDynamicQR(amount: String) : Bitmap {
        return qrRepository.generateDynamicQR(amount)
    }

    fun getMerchant(callback: (Merchant?) -> Unit){
        qrRepository.getMerchant  { merchant ->
            callback(merchant)
        }
    }
}