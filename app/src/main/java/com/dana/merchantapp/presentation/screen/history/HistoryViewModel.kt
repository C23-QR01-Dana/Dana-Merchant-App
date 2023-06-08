package com.dana.merchantapp.presentation.screen.history

import com.dana.merchantapp.domain.history.HistoryUseCase
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dana.merchantapp.data.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor (private val historyUseCase: HistoryUseCase) : ViewModel() {

    private val _transactions = mutableStateOf<List<Transaction>?>(null)
    val transactions: State<List<Transaction>?> get() = _transactions

    fun getTransactionsFromFirestore() {
        viewModelScope.launch {
            historyUseCase.getTransactionsFromFirestore { transactions ->
                if (transactions != null) {
                    _transactions.value = transactions
                } else {
                    _transactions.value = null
                }
            }
        }
    }

    fun convertTimestampToDayMonthYear(timestamp: Long): String {
        return historyUseCase.convertTimestampToDayMonthYear(timestamp)
    }

    fun convertTimestampToHourMinute(timestamp: Long): String {
        return historyUseCase.convertTimestampToHourMinute(timestamp)
    }
}