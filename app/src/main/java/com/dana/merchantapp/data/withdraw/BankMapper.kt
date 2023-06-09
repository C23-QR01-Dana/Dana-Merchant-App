package com.dana.merchantapp.data.withdraw

import com.dana.merchantapp.data.model.BankAccount
import com.google.firebase.firestore.DocumentSnapshot

object BankMapper {
    fun mapToBank(documentSnapshot: DocumentSnapshot): BankAccount {
        return if (documentSnapshot.exists()) {
            val accountName = documentSnapshot.getString("accountName") ?: ""
            val bankAccountNo = documentSnapshot.getString("bankAccountNo") ?: ""
            val bankInst = documentSnapshot.getString("bankInst") ?: ""

            BankAccount(accountName, bankAccountNo, bankInst)
        } else {
            BankAccount("", "", "")
        }
    }
}