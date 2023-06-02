package com.dana.merchantapp.data.qr

import android.graphics.Bitmap

interface QRRepository {
    fun generateStaticQR(): Bitmap
}