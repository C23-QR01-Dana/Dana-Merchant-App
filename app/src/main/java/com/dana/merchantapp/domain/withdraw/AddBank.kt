package com.dana.merchantapp.domain.withdraw

import com.dana.merchantapp.data.model.BankAccount
import javax.inject.Inject

class AddBank @Inject constructor (private val withdrawRepository: WithdrawRepository) {
    fun addBank(bankAccount: BankAccount, callback: (Boolean) -> Unit) {
        withdrawRepository.addBank(bankAccount, callback)
    }
}