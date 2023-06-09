package com.dana.merchantapp.presentation.screen.qr.staticqr

import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dana.merchantapp.data.model.Merchant
import com.dana.merchantapp.domain.home.GetMerchant
import com.dana.merchantapp.domain.qr.GenerateStaticQR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaticQrViewModel @Inject constructor(
    private val generateStaticQR: GenerateStaticQR,
    private val getMerchant: GetMerchant
    ) : ViewModel() {

    private val _qrCodeBitmap = mutableStateOf<Bitmap?>(null)
    val qrCodeBitmap: State<Bitmap?> get() = _qrCodeBitmap

    private val _merchant = mutableStateOf<Merchant?>(null)
    val merchant: State<Merchant?> get() = _merchant

    fun generateStaticQR() {
        _qrCodeBitmap.value = generateStaticQR.generateStaticQR()
    }

    fun getMerchant() {
        viewModelScope.launch {
            getMerchant.getMerchant { merchant ->
                if (merchant != null) {
                    _merchant.value = merchant
                } else {
                    _merchant.value = null
                }
            }
        }
    }


}