package com.dana.merchantapp.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dana.merchantapp.domain.splash.CheckIsLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor (private val checkIsLogin: CheckIsLogin) : ViewModel() {
    private val _isLogin: MutableLiveData<Boolean> = MutableLiveData()
    val isLogin: LiveData<Boolean> = _isLogin
    init {
        checkIsLogin()
    }
    fun checkIsLogin() {
        // Panggil metode registerUser di use case
        checkIsLogin.checkIsLogin() { isLogin ->
            _isLogin.value = isLogin

        }
    }


}