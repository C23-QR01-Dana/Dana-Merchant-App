package com.dana.merchantapp.domain.history

import com.dana.merchantapp.data.model.Transaction
import javax.inject.Inject

class GetIncomeOutcome @Inject constructor (private val historyRepository: HistoryRepository) {
    fun getIncomeOutcome(fetchedTransactions: List<Transaction>): HashMap<String, String> {
        return historyRepository.getIncomeOutcome(fetchedTransactions)
    }
}