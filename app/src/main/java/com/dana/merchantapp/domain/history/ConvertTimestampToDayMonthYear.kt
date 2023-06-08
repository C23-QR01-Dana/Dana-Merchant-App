package com.dana.merchantapp.domain.history

import javax.inject.Inject

class ConvertTimestampToDayMonthYear @Inject constructor (private val historyRepository: HistoryRepository) {
    fun convertTimestampToDayMonthYear(timestamp: Long): String {
        return historyRepository.convertTimestampToDayMonthYear(timestamp)
    }
}