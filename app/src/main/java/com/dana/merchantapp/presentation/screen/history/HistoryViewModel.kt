package com.dana.merchantapp.presentation.screen.history

import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dana.merchantapp.data.model.Transaction
import com.dana.merchantapp.domain.history.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor (
    private val getTransactions: GetTransactions,
    private val applyFilters: ApplyFilters,
    private val getIncomeOutcome: GetIncomeOutcome,
    private val convertTimestampToHourMinute: ConvertTimestampToHourMinute,
    private val convertTimestampToDayMonthYear: ConvertTimestampToDayMonthYear)
    : ViewModel() {

    private val _transactions = mutableStateOf<List<Transaction>?>(null)
    val transactions: State<List<Transaction>?> get() = _transactions

    private val _recentIncome = mutableStateOf<String?>("0")
    val recentIncome: State<String?> get() = _recentIncome

    private val _recentOutcome = mutableStateOf<String?>("0")
    val recentOutcome: State<String?> get() = _recentOutcome

    var minAmount = mutableStateOf(0L)
    var maxAmount = mutableStateOf(Long.MAX_VALUE)
    var startDate = mutableStateOf(0L)
    var endDate = mutableStateOf(Long.MAX_VALUE)
    val transactionType = mutableStateOf("All")

    fun getTransactions() {
        viewModelScope.launch {
            getTransactions.getTransactions { transactions ->
                if (transactions != null) {
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_YEAR, -30)
                    val timestamp30DaysAgo = calendar.timeInMillis

                    val recentTransactions = applyFilters.applyFilters(transactions, 0L, Long.MAX_VALUE, timestamp30DaysAgo, Long.MAX_VALUE, "All")
                    val hashMapIncomeOutcome = getIncomeOutcome.getIncomeOutcome(recentTransactions)
                    _recentIncome.value = hashMapIncomeOutcome["income"] ?: "0"
                    _recentOutcome.value = hashMapIncomeOutcome["outcome"] ?: "0"

                    val filteredTransactions = applyFilters.applyFilters(transactions, minAmount.value, maxAmount.value, startDate.value, endDate.value, transactionType.value)
                    _transactions.value = filteredTransactions
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