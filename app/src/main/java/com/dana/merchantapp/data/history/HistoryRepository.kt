package com.dana.merchantapp.data.history

import com.dana.merchantapp.presentation.model.Transaction

interface HistoryRepository {
    fun getTransactionsFromFirestore(callback: (List<Transaction>?) -> Unit)

    fun convertTimestampToMonthYear(timestamp: Long): String

    fun convertTimestampToFullDateTime(timestamp: Long): String
}
