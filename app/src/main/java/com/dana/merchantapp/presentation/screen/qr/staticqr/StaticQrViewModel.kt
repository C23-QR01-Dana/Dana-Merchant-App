package com.dana.merchantapp.presentation.screen.qr.staticqr

import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dana.merchantapp.domain.qr.QRUseCase
import com.dana.merchantapp.presentation.model.Merchant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaticQrViewModel @Inject constructor(private val qrUseCase: QRUseCase) : ViewModel() {

    private val _qrCodeBitmap = mutableStateOf<Bitmap?>(null)
    val qrCodeBitmap: State<Bitmap?> get() = _qrCodeBitmap

    private val _merchant = mutableStateOf<Merchant?>(null)
    val merchant: State<Merchant?> get() = _merchant


    fun generateStaticQR() {
        _qrCodeBitmap.value = qrUseCase.generateStaticQR()
    }

    fun getMerchant() {
        viewModelScope.launch {
            qrUseCase.getMerchant { merchant ->
                if (merchant != null) {
                    _merchant.value = merchant
                } else {
                    _merchant.value = null
                }
            }
        }
    }


}