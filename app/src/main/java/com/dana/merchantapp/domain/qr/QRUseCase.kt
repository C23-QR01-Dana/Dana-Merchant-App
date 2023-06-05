package com.dana.merchantapp.domain.qr

import android.graphics.Bitmap
import com.dana.merchantapp.data.qr.QRRepository
import com.dana.merchantapp.presentation.model.Merchant
import javax.inject.Inject


class QRUseCase  @Inject constructor (private val qrRepository: QRRepository) {
    fun generateStaticQR() : Bitmap {
        return qrRepository.generateStaticQR()
    }
    fun getMerchant(callback: (Merchant?) -> Unit){
        qrRepository.getMerchant  { merchant ->
            callback(merchant)
        }
    }

    fun transaction(amount: Int, payerId: String, timestamp: Long, trxType: String, callback: (Boolean) -> Unit){
        qrRepository.transaction(amount, payerId, timestamp, trxType) { isSuccess->
            callback(isSuccess)
        }
    }
}