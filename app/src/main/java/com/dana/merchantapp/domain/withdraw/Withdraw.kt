package com.dana.merchantapp.domain.withdraw

import com.dana.merchantapp.data.model.BankAccount
import javax.inject.Inject

class Withdraw @Inject constructor (private val withdrawRepository: WithdrawRepository) {
    fun withdraw(amount: Long, timestamp: Long, bankAccount: BankAccount, callback: (Boolean) -> Unit) {
        withdrawRepository.withdraw(amount, timestamp, bankAccount, callback)
    }
}