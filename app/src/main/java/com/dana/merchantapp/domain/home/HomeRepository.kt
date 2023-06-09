package com.dana.merchantapp.domain.home

import com.dana.merchantapp.data.model.Merchant

interface HomeRepository {
    fun getMerchant(callback: (Merchant?) -> Unit)
}