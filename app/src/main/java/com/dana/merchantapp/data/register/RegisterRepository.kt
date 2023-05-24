package com.dana.merchantapp.data.register

interface RegisterRepository {
    fun registerUser(name: String, address: String,email: String, password: String, callback: (Boolean, String) -> Unit)
}

