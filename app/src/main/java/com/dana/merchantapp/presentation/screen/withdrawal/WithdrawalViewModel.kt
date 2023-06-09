package com.dana.merchantapp.presentation.screen.withdrawal

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dana.merchantapp.data.model.BankAccount
import com.dana.merchantapp.data.model.Merchant
import com.dana.merchantapp.data.model.Transaction
import com.dana.merchantapp.domain.history.ApplyFilters
import com.dana.merchantapp.domain.history.ConvertTimestampToDayMonthYear
import com.dana.merchantapp.domain.history.ConvertTimestampToHourMinute
import com.dana.merchantapp.domain.history.GetTransactions
import com.dana.merchantapp.domain.home.GetMerchant
import com.dana.merchantapp.domain.profile.LogoutUser
import com.dana.merchantapp.domain.profile.UpdatePhoto
import com.dana.merchantapp.domain.withdraw.AddBank
import com.dana.merchantapp.domain.withdraw.GetMerchantBank
import com.dana.merchantapp.domain.withdraw.ValidateBank
import com.dana.merchantapp.domain.withdraw.Withdraw
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawalViewModel @Inject constructor(
    private val getMerchant: GetMerchant,
    private val getMerchantBank: GetMerchantBank,
    private val validateBank: ValidateBank,
    private val addBank: AddBank,
    private val withdraw: Withdraw,
    private val getTransactions: GetTransactions,
    private val applyFilters: ApplyFilters,
    private val convertTimestampToHourMinute: ConvertTimestampToHourMinute,
    private val convertTimestampToDayMonthYear: ConvertTimestampToDayMonthYear
    ) : ViewModel() {

    private val _merchant = mutableStateOf<Merchant?>(null)
    val merchant: State<Merchant?> get() = _merchant

    private val _bankList = mutableStateOf<ArrayList<BankAccount>>(ArrayList())
    val bankList: State<ArrayList<BankAccount>> get() = _bankList

    private val _validAccount = mutableStateOf<BankAccount?>(null)
    val validAccount: State<BankAccount?> get() = _validAccount

    private val _isAccountValid = mutableStateOf<Boolean>(false)
    val isAccountValid: State<Boolean> get() = _isAccountValid

    private val _initialTrue = mutableStateOf<Boolean>(true)
    val initialTrue: State<Boolean> get() = _initialTrue

    private val _isBankAdded = MutableStateFlow(false)
    val isBankAdded: MutableStateFlow<Boolean> get() = _isBankAdded

    private val _isWithdrawSuccess = MutableStateFlow(false)
    val isWithdrawSuccess: MutableStateFlow<Boolean> get() = _isWithdrawSuccess

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

    fun getMerchantBank() {
        getMerchantBank.getMerchantBank { isSuccess, bankList ->
            if (isSuccess) {
                _bankList.value = bankList
            } else {
                _bankList.value = ArrayList()
            }
        }
    }

    fun validateBank(bankName: String, accountNo: String) {
        validateBank.validateBank (bankName, accountNo) { isSuccess, bankAccount ->
            _validAccount.value = bankAccount
            _isAccountValid.value = isSuccess
            _initialTrue.value = isSuccess
            Log.v("WithdrawalViewModel", "validateBank: $isSuccess")
        }
    }

    fun addBank(bankAccount: BankAccount) {
        addBank.addBank(bankAccount) { isSuccess ->
            _isBankAdded.value = isSuccess
        }
    }

    fun withdraw(amount: Long, timestamp: Long, bankAccount: BankAccount) {
        withdraw.withdraw(amount, timestamp, bankAccount) { isSuccess ->
            _isWithdrawSuccess.value = isSuccess
        }
    }

    fun getTransactions() {
        viewModelScope.launch {
            getTransactions.getTransactions { transactions ->
                if (transactions != null) {
                    _transactions.value = applyFilters.applyFilters(transactions, 0L, Long.MAX_VALUE, 0L, Long.MAX_VALUE, "Outgoing")
                } else {
                    _transactions.value = null
                }
            }
        }
    }

    fun convertTimestampToDayMonthYear(timestamp: Long): String {
        return convertTimestampToDayMonthYear.convertTimestampToDayMonthYear(timestamp)
    }

    fun convertTimestampToHourMinute(timestamp: Long): String {
        return convertTimestampToHourMinute.convertTimestampToHourMinute(timestamp)
    }

    fun makeInvalid() {
        _isAccountValid.value = false
        _validAccount.value = null
        Log.v("WithdrawalViewModel", "makeInvalid ketrigger")
    }
}