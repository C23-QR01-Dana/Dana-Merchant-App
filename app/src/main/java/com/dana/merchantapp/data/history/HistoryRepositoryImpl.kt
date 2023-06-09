package com.dana.merchantapp.data.history

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale
import com.dana.merchantapp.data.model.Transaction
import com.dana.merchantapp.domain.history.HistoryRepository
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor (private val auth: FirebaseAuth, private val firestore: FirebaseFirestore):
    HistoryRepository {

    override fun convertTimestampToDayMonthYear(timestamp: Long): String {
        val adjustedTimestamp = if (timestamp.toString().length <= 10) {
            timestamp * 1000
        } else {
            timestamp
        }

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return dateFormat.format(adjustedTimestamp)
    }

    override fun convertTimestampToHourMinute(timestamp: Long): String {
        val adjustedTimestamp = if (timestamp.toString().length <= 10) {
            timestamp * 1000
        } else {
            timestamp
        }

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(adjustedTimestamp)
    }

    override fun getTransactions(callback: (List<Transaction>?) -> Unit) {
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

    override fun applyFilters(fetchedTransactions: List<Transaction>, minAmount: Long, maxAmount: Long, startDate: Long, endDate: Long, transactionType: String): List<Transaction> {
        val filteredTransactions = fetchedTransactions.filter { transaction ->
            val amount = transaction.amount ?: 0
            val timestamp = transaction.timestamp
            val type = transaction.trxType

            val isAmountInRange = amount in minAmount..maxAmount

            // Check if timestamp is in seconds or milliseconds and convert to milliseconds if needed
            val timestampMillis = if (timestamp != null && timestamp.toString().length == 10) {
                timestamp * 1000
            } else {
                timestamp
            }

            val isTimestampInRange = if (startDate != 0L && endDate != Long.MAX_VALUE) {
                timestampMillis != null && timestampMillis >= startDate && timestampMillis <= endDate
            } else {
                true
            }

            val isTypeMatched = when (transactionType) {
                "Incoming" -> type == "PAYMENT"
                "Outgoing" -> type == "MERCHANT_WITHDRAW"
                else -> true
            }

            isAmountInRange && isTimestampInRange && isTypeMatched
        }
        return filteredTransactions
    }

}