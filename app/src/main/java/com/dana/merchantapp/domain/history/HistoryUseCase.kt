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

    fun convertTimestampToDayMonthYear(timestamp: Long): String {
        return historyRepository.convertTimestampToDayMonthYear(timestamp)
    }

    fun convertTimestampToHourMinute(timestamp: Long): String {
        return historyRepository.convertTimestampToHourMinute(timestamp)
    }
}
