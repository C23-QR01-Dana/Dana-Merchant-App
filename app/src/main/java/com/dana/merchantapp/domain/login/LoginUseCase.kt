package com.dana.merchantapp.domain.login

import com.dana.merchantapp.data.login.LoginRepository

class LoginUseCase(private val loginRepository: LoginRepository) {
    fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        loginRepository.loginUser(email, password) { isSuccess, message ->
            callback(isSuccess, message)
        }
    }
}