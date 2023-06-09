package com.dana.merchantapp.domain.history

import com.dana.merchantapp.data.model.Transaction
import javax.inject.Inject

class ApplyFilters @Inject constructor (private val historyRepository: HistoryRepository) {
    fun applyFilters(fetchedTransactions: List<Transaction>, minAmount: Long, maxAmount: Long, startDate: Long, endDate: Long, transactionType: String): List<Transaction> {
        return historyRepository.applyFilters(fetchedTransactions, minAmount, maxAmount, startDate, endDate, transactionType)
    }
}