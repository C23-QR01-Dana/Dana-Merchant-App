package com.dana.merchantapp.data.history

import com.dana.merchantapp.presentation.model.MerchantWithdrawTransaction
import com.dana.merchantapp.presentation.model.PaymentTransaction
import com.dana.merchantapp.presentation.model.Transaction
import com.google.firebase.firestore.DocumentSnapshot

//object TransactionMapper {
//    fun mapToTransaction(documentSnapshot: DocumentSnapshot): Transaction? {
//        return if (documentSnapshot.exists()) {
//            val amount = documentSnapshot.getLong("amount")?.toInt() ?: 0
//            val id = documentSnapshot.getString("id") ?: ""
//            val merchantId = documentSnapshot.getString("merchantId") ?: ""
//            val payerId = documentSnapshot.getString("payerId") ?: ""
//            val timestamp = documentSnapshot.getLong("timestamp") ?: 0.toLong()
//            val trxType = documentSnapshot.getString("trxType") ?: ""
//
//            Transaction(amount, id, merchantId, payerId, timestamp, trxType)
//        } else {
//            null
//        }
//    }
//}

object TransactionMapper {
    fun mapToTransaction(documentSnapshot: DocumentSnapshot): Transaction? {
        return if (documentSnapshot.exists()) {
            val amount = documentSnapshot.getLong("amount")?.toInt() ?: 0
            val id = documentSnapshot.getString("id") ?: ""
            val merchantId = documentSnapshot.getString("merchantId") ?: ""
            val timestamp = documentSnapshot.getLong("timestamp") ?: 0.toLong()
            val trxType = documentSnapshot.getString("trxType") ?: ""

            when (trxType) {
                "PAYMENT" -> {
                    val payerId = documentSnapshot.getString("payerId") ?: ""
                    PaymentTransaction(amount, id, merchantId, timestamp, trxType, payerId)
                }
                "MERCHANT_WITHDRAW" -> {
                    val bankAccountNo = documentSnapshot.getString("bankAccountNo") ?: ""
                    val bankInst = documentSnapshot.getString("bankInst") ?: ""
                    MerchantWithdrawTransaction(amount, id, merchantId, timestamp, trxType, bankAccountNo, bankInst)
                }
                else -> null
            }
        } else {
            null
        }
    }
}
