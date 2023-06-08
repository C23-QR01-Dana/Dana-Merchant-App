package com.dana.merchantapp.presentation.screen.history

import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dana.merchantapp.data.model.Transaction
import com.dana.merchantapp.domain.history.ConvertTimestampToDayMonthYear
import com.dana.merchantapp.domain.history.ConvertTimestampToHourMinute
import com.dana.merchantapp.domain.history.GetTransactions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor (
    private val getTransactions: GetTransactions,
    private val convertTimestampToHourMinute: ConvertTimestampToHourMinute,
    private val convertTimestampToDayMonthYear: ConvertTimestampToDayMonthYear)
    : ViewModel() {

    private val _transactions = mutableStateOf<List<Transaction>?>(null)
    val transactions: State<List<Transaction>?> get() = _transactions

    fun getTransactions() {
        viewModelScope.launch {
            getTransactions.getTransactions { transactions ->
                if (transactions != null) {
                    _transactions.value = transactions
                } else {
                    _transactions.value = null
                }
            }
        }
    }

    fun convertTimestampToHourMinute(timestamp: Long): String {
        return convertTimestampToHourMinute.convertTimestampToHourMinute(timestamp)
    }

    fun convertTimestampToDayMonthYear(timestamp: Long): String {
        return convertTimestampToDayMonthYear.convertTimestampToDayMonthYear(timestamp)
    }
}