package com.dana.merchantapp.domain.withdraw

import com.dana.merchantapp.data.model.BankAccount
import javax.inject.Inject

class GetMerchantBank @Inject constructor (private val withdrawRepository: WithdrawRepository) {
    fun getMerchantBank(callback: (Boolean, ArrayList<BankAccount>) -> Unit) {
        withdrawRepository.getMerchantBank(callback)
    }
}