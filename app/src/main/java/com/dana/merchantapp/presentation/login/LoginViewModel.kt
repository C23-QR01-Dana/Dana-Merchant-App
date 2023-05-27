package com.dana.merchantapp.presentation.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dana.merchantapp.domain.login.LoginUseCase

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel(){
    private val _loginResult: MutableLiveData<Boolean> = MutableLiveData()
    val loginResult: LiveData<Boolean> = _loginResult
    private val _loginMessage: MutableLiveData<String> = MutableLiveData()
    val loginMessage: LiveData<String> = _loginMessage

    fun loginUser(email: String, password: String) {
        loginUseCase.loginUser(email, password) { isSuccess, message ->
            _loginResult.value = isSuccess
            _loginMessage.value = message
        }
    }
}