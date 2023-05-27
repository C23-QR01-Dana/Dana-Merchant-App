package com.dana.merchantapp.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dana.merchantapp.domain.main.MainUseCase

class MainViewModel(private val mainUseCase: MainUseCase) {
    private val _logoutResult: MutableLiveData<Boolean> = MutableLiveData()
    val logoutResult: LiveData<Boolean> = _logoutResult
    private val _logoutMessage: MutableLiveData<String> = MutableLiveData()
    val logoutMessage: LiveData<String> = _logoutMessage

    fun logoutUser() {
        mainUseCase.logoutUser { isSuccess, message ->
            _logoutMessage.value = message
            _logoutResult.value = isSuccess
        }
    }
}