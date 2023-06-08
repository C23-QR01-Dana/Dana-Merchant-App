package com.dana.merchantapp.domain.history

import javax.inject.Inject

class ConvertTimestampToHourMinute @Inject constructor (private val historyRepository: HistoryRepository) {
    fun convertTimestampToHourMinute(timestamp: Long): String {
        return historyRepository.convertTimestampToHourMinute(timestamp)
    }
}