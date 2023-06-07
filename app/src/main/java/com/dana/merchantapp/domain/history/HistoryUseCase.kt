package com.dana.merchantapp.domain.history


import com.dana.merchantapp.data.history.HistoryRepository
import javax.inject.Inject

import kotlin.Unit
import com.dana.merchantapp.presentation.model.Transaction

class HistoryUseCase @Inject constructor (private val historyRepository:HistoryRepository){
    fun getTransactionsFromFirestore(callback: (List<Transaction>?) -> Unit) {
        historyRepository.getTransactionsFromFirestore { transactions ->
            callback(transactions)
        }
    }

    fun convertTimestampToMonthYear(timestamp: Long): String {
        return historyRepository.convertTimestampToMonthYear(timestamp)
    }

    fun convertTimestampToFullDateTime(timestamp: Long): String {
        return historyRepository.convertTimestampToFullDateTime(timestamp)
    }
}
