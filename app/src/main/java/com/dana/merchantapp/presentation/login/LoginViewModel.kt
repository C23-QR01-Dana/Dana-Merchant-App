package com.dana.merchantapp.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dana.merchantapp.domain.login.LoginUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUser: LoginUser) : ViewModel(){
    private val _loginResult: MutableLiveData<Boolean> = MutableLiveData()
    val loginResult: LiveData<Boolean> = _loginResult
    private val _loginMessage: MutableLiveData<String> = MutableLiveData()
    val loginMessage: LiveData<String> = _loginMessage

    fun loginUser(email: String, password: String) {
        loginUser.loginUser(email, password) { isSuccess, message ->
            _loginResult.value = isSuccess
            _loginMessage.value = message
        }
    }
}