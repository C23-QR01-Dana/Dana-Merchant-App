package com.dana.merchantapp.data.history

import com.dana.merchantapp.data.model.Transaction

interface HistoryRepository {
    fun getTransactionsFromFirestore(callback: (List<Transaction>?) -> Unit)

    fun convertTimestampToDayMonthYear(timestamp: Long): String

    fun convertTimestampToHourMinute(timestamp: Long): String
}
