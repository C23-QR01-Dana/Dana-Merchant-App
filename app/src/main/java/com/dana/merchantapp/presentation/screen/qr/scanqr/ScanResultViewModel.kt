package com.dana.merchantapp.presentation.screen.qr.scanqr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dana.merchantapp.domain.qr.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanResultViewModel @Inject constructor(private val transaction: Transaction) : ViewModel() {
    private val _transactionResult: MutableLiveData<Boolean> = MutableLiveData()
    val transactionResult: LiveData<Boolean> = _transactionResult

    fun transaction(amount: Int, payerId: String, timestamp: Long, trxType: String) {
        transaction.transaction(amount, payerId, timestamp, trxType) { isSuccess->
            _transactionResult.value = isSuccess
        }
    }


}
