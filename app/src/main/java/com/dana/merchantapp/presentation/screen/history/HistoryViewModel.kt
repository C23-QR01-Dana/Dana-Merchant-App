package com.dana.merchantapp.presentation.screen.history

import com.dana.merchantapp.domain.history.HistoryUseCase
import com.dana.merchantapp.presentation.model.Transaction
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun convertTimestampToMonthYear(timestamp: Long): String {
        return historyUseCase.convertTimestampToMonthYear(timestamp)
    }

    fun convertTimestampToFullDateTime(timestamp: Long): String {
        return historyUseCase.convertTimestampToFullDateTime(timestamp)
    }
}

//@HiltViewModel
//class StaticQrViewModel @Inject constructor(private val qrUseCase: QRUseCase) : ViewModel() {
//
//    private val _qrCodeBitmap = mutableStateOf<Bitmap?>(null)
//    val qrCodeBitmap: State<Bitmap?> get() = _qrCodeBitmap
//
//    private val _merchant = mutableStateOf<Merchant?>(null)
//    val merchant: State<Merchant?> get() = _merchant
//
//
//    fun generateStaticQR() {
//        _qrCodeBitmap.value = qrUseCase.generateStaticQR()
//    }
//
//    fun getMerchant() {
//        viewModelScope.launch {
//            qrUseCase.getMerchant { merchant ->
//                if (merchant != null) {
//                    _merchant.value = merchant
//                } else {
//                    _merchant.value = null
//                }
//            }
//        }
//    }
//
//
//}