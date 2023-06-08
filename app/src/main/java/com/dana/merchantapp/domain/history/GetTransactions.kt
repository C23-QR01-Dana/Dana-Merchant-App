package com.dana.merchantapp.domain.history

import com.dana.merchantapp.data.model.Transaction
import javax.inject.Inject

class GetTransactions @Inject constructor (private val historyRepository: HistoryRepository) {
    fun getTransactions(callback: (List<Transaction>?) -> Unit) {
        historyRepository.getTransactions { transactions ->
            callback(transactions)
        }
    }
}