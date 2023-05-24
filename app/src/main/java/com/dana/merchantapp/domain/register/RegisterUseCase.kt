package com.dana.merchantapp.domain.register

import com.dana.merchantapp.data.register.RegisterRepository

class RegisterUseCase(private val registerRepository: RegisterRepository) {
    fun registerUser(name: String, address: String,email: String, password: String, callback: (Boolean, String) -> Unit) {
        // Panggil metode registerUser di repository
        registerRepository.registerUser(name, address, email, password) { isSuccess, message ->
            callback(isSuccess, message)
        }
    }
}