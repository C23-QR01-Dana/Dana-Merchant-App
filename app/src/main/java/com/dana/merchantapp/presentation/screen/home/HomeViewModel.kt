package com.dana.merchantapp.presentation.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dana.merchantapp.data.model.Merchant
import com.dana.merchantapp.data.model.Transaction
import com.dana.merchantapp.domain.history.ApplyFilters
import com.dana.merchantapp.domain.history.ConvertTimestampToDayMonthYear
import com.dana.merchantapp.domain.history.ConvertTimestampToHourMinute
import com.dana.merchantapp.domain.history.GetTransactions
import com.dana.merchantapp.domain.home.GetMerchant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTransactions: GetTransactions,
    private val getMerchant: GetMerchant,
    private val convertTimestampToHourMinute: ConvertTimestampToHourMinute,
    ) : ViewModel() {

    private val _merchant = mutableStateOf<Merchant?>(null)
    val merchant: State<Merchant?> get() = _merchant

    private val _transactions = mutableStateOf<List<Transaction>?>(null)
    val transactions: State<List<Transaction>?> get() = _transactions

    fun getMerchant() {
        getMerchant.getMerchant { merchant ->
            if (merchant != null) {
                _merchant.value = merchant
            } else {
                _merchant.value = null
            }
        }
    }

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
}