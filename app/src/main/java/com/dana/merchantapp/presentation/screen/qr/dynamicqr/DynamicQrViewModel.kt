package com.dana.merchantapp.presentation.screen.qr.dynamicqr

import androidx.compose.runtime.*
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dana.merchantapp.data.model.Merchant
import com.dana.merchantapp.domain.home.GetMerchant
import com.dana.merchantapp.domain.qr.GenerateDynamicQR
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DynamicQrViewModel @Inject constructor(
    private val generateDynamicQR: GenerateDynamicQR,
    private val getMerchant: GetMerchant
    ) : ViewModel() {

    private val _qrCodeBitmap = mutableStateOf<Bitmap?>(null)
    val qrCodeBitmap: State<Bitmap?> get() = _qrCodeBitmap

    private val _merchant = mutableStateOf<Merchant?>(null)
    val merchant: State<Merchant?> get() = _merchant

    fun generateDynamicQR(amount: String) {
        _qrCodeBitmap.value = generateDynamicQR.generateDynamicQR(amount)
    }

    fun getMerchant() {
        getMerchant.getMerchant { merchant ->
            if (merchant != null) {
                _merchant.value = merchant
            } else {
                _merchant.value = null
            }
        }
    }
}