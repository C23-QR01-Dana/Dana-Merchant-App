package com.dana.merchantapp.presentation.register

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.dana.merchantapp.domain.register.RegisterUseCase
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {
    fun registerUser(name: String, address: String,email: String, password: String) {
        // Panggil metode registerUser di use case
        registerUseCase.registerUser(name,address, email, password)
    }
}


