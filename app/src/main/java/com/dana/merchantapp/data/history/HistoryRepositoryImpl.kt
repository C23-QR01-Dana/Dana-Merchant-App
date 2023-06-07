package com.dana.merchantapp.data.history

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale
import com.dana.merchantapp.presentation.model.Transaction

class HistoryRepositoryImpl: HistoryRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun convertTimestampToMonthYear(timestamp: Long): String {
        val adjustedTimestamp = if (timestamp.toString().length <= 10) {
            timestamp * 1000
        } else {
            timestamp
        }

        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        return dateFormat.format(adjustedTimestamp)
    }

    override fun convertTimestampToFullDateTime(timestamp: Long): String {
        val adjustedTimestamp = if (timestamp.toString().length <= 10) {
            timestamp * 1000
        } else {
            timestamp
        }

        val dateFormat = SimpleDateFormat("dd MMMM yyyy • HH:mm", Locale.getDefault())
        return dateFormat.format(adjustedTimestamp)
    }

    override fun getTransactionsFromFirestore(callback: (List<Transaction>?) -> Unit) {
        val transactionsRef = firestore.collection("transaction")
        val transactions = mutableListOf<Transaction>()

        // Filter transactions based on merchantId
        transactionsRef.whereEqualTo("merchantId", auth.currentUser?.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        val transaction = TransactionMapper.mapToTransaction(document)
                        transactions.add(transaction!!)
                    }
                    callback(transactions)
                }
                else {
                    callback(null)
                }
            }
    }

}