package com.dana.merchantapp.domain.withdraw

import com.dana.merchantapp.data.model.BankAccount
import javax.inject.Inject

class ValidateBank @Inject constructor (private val withdrawRepository: WithdrawRepository) {
    fun validateBank(bankName: String, accountNo: String, callback: (Boolean, BankAccount) -> Unit) {
        withdrawRepository.validateBank(bankName, accountNo, callback)
    }
}