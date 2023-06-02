package com.dana.merchantapp.presentation.screen.qr.staticqr

import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dana.merchantapp.domain.qr.QRUseCase

class StaticQrViewModel(private val qrUseCase: QRUseCase) : ViewModel() {

    private val _qrCodeBitmap = mutableStateOf<Bitmap?>(null)
    val qrCodeBitmap: State<Bitmap?> get() = _qrCodeBitmap


    fun generateStaticQR() {
        _qrCodeBitmap.value = qrUseCase.generateStaticQR()
    }

}