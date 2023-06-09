package com.dana.merchantapp.domain.history

import com.dana.merchantapp.data.model.Transaction

interface HistoryRepository {
    fun getTransactions(callback: (List<Transaction>?) -> Unit)

    fun applyFilters(fetchedTransactions: List<Transaction>, minAmount: Long, maxAmount: Long, startDate: Long, endDate: Long, transactionType: String): List<Transaction>

    fun getIncomeOutcome(fetchedTransations: List<Transaction>): HashMap<String, String>

    fun convertTimestampToDayMonthYear(timestamp: Long): String

    fun convertTimestampToHourMinute(timestamp: Long): String
}
