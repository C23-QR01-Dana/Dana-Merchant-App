package com.dana.merchantapp.presentation.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dana.merchantapp.data.model.Merchant
import com.dana.merchantapp.domain.home.GetMerchant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMerchant: GetMerchant
    ) : ViewModel() {

    private val _merchant = mutableStateOf<Merchant?>(null)
    val merchant: State<Merchant?> get() = _merchant

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