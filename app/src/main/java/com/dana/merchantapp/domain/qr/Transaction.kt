package com.dana.merchantapp.domain.qr

import javax.inject.Inject


class Transaction @Inject constructor (private val qrRepository: QRRepository) {
    fun transaction(amount: Int, payerId: String, timestamp: Long, trxType: String, callback: (Boolean) -> Unit){
        qrRepository.transaction(amount, payerId, timestamp, trxType) { isSuccess->
            callback(isSuccess)
        }
    }
}