package com.dana.merchantapp.data.login

interface LoginRepository {
    fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit)
}