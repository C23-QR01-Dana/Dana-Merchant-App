package com.dana.merchantapp.presentation.register


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.dana.merchantapp.domain.register.RegisterUseCase


class RegisterViewModel(private val context: Context, private val registerUseCase: RegisterUseCase) : ViewModel() {
    fun registerUser(name: String, address: String, email: String, password: String) {
        // Panggil metode registerUser di use case
        registerUseCase.registerUser(name, address, email, password) { isSuccess,message ->
            if (isSuccess) {
                showToast(message)
            } else {
                showToast(message)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
}


