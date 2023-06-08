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

    var minAmount = mutableStateOf(0L)
    var maxAmount = mutableStateOf(Long.MAX_VALUE)
    var startDate = mutableStateOf(0L)
    var endDate = mutableStateOf(Long.MAX_VALUE)
    val transactionType = mutableStateOf("All")

    fun getTransactions() {
        viewModelScope.launch {
            getTransactions.getTransactions { transactions ->
                if (transactions != null) {
                    applyFilters(transactions)
                } else {
                    _transactions.value = null
                }
            }
        }
    }

    fun applyFilters(fetchedTransactions: List<Transaction>) {
        val filteredTransactions = fetchedTransactions.filter { transaction ->
            val amount = transaction.amount ?: 0
            val timestamp = transaction.timestamp
            val type = transaction.trxType // Assuming you have a 'type' property in your Transaction model

            val isAmountInRange = amount >= minAmount.value && amount <= maxAmount.value

            // Check if timestamp is in seconds or milliseconds and convert to milliseconds if needed
            val timestampMillis = if (timestamp != null && timestamp.toString().length == 10) {
                timestamp * 1000
            } else {
                timestamp
            }

            val isTimestampInRange = if (startDate.value != 0L && endDate.value != Long.MAX_VALUE) {
                timestampMillis != null && timestampMillis >= startDate.value && timestampMillis <= endDate.value
            } else {
                true
            }

            val isTypeMatched = when (transactionType.value) {
                "Incoming" -> type == "PAYMENT"
                "Outgoing" -> type == "MERCHANT_WITHDRAW"
                else -> true
            }

            isAmountInRange && isTimestampInRange && isTypeMatched
        }
        _transactions.value = filteredTransactions
    }



    fun convertTimestampToHourMinute(timestamp: Long): String {
        return convertTimestampToHourMinute.convertTimestampToHourMinute(timestamp)
    }

    fun convertTimestampToDayMonthYear(timestamp: Long): String {
        return convertTimestampToDayMonthYear.convertTimestampToDayMonthYear(timestamp)
    }



}