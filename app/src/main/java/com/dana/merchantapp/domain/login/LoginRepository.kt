package com.dana.merchantapp.domain.login

interface LoginRepository {
    fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit)
}