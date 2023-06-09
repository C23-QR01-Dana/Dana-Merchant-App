package com.dana.merchantapp.domain.withdraw

import com.dana.merchantapp.data.model.BankAccount
import java.sql.Timestamp

interface WithdrawRepository {
    fun getMerchantBank(callback: (Boolean, ArrayList<BankAccount>) -> Unit)
    fun validateBank(bankName: String, accountNo: String,callback: (Boolean, BankAccount) -> Unit)
    fun addBank(bankAccount: BankAccount, callback: (Boolean) -> Unit)
    fun withdraw(amount: Long, timestamp: Long, bankAccount: BankAccount, callback: (Boolean) -> Unit)
}