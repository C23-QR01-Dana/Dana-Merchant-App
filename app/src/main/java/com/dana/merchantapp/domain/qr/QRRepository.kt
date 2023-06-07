package com.dana.merchantapp.domain.qr

import android.graphics.Bitmap
import com.dana.merchantapp.data.model.Merchant

interface QRRepository {
    fun generateStaticQR(): Bitmap
    fun generateDynamicQR(amount:String): Bitmap
    fun getMerchant(callback: (Merchant?) -> Unit)

    fun transaction(amount: Int, payerId: String, timestamp: Long, trxType: String, callback: (Boolean) -> Unit)
}