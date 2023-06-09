package com.dana.merchantapp.domain.login

import javax.inject.Inject

class LoginUser @Inject constructor (private val loginRepository: LoginRepository) {
    fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        loginRepository.loginUser(email, password) { isSuccess, message ->
            callback(isSuccess, message)
        }
    }
}