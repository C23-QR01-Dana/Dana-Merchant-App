package com.dana.merchantapp.domain.home

import com.dana.merchantapp.data.model.Merchant
import com.dana.merchantapp.domain.home.HomeRepository
import com.dana.merchantapp.domain.qr.QRRepository
import javax.inject.Inject

class GetMerchant @Inject constructor (private val homeRepository: HomeRepository) {
    fun getMerchant(callback: (Merchant?) -> Unit){
        homeRepository.getMerchant  { merchant ->
            callback(merchant)
        }
    }
}