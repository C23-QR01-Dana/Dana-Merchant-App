package com.dana.merchantapp.domain.register

interface RegisterRepository {
    fun registerUser(name: String, address: String,email: String, password: String, callback: (Boolean, String) -> Unit)
}

