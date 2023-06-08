package com.dana.merchantapp.domain.history

import com.dana.merchantapp.data.model.Transaction

interface HistoryRepository {
    fun getTransactions(callback: (List<Transaction>?) -> Unit)

    fun convertTimestampToDayMonthYear(timestamp: Long): String

    fun convertTimestampToHourMinute(timestamp: Long): String
}
