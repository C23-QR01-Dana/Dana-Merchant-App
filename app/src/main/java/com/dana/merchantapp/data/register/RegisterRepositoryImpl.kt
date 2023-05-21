package com.dana.merchantapp.data.register

import android.util.Log
import com.google.firebase.auth.FirebaseAuth


class RegisterRepositoryImpl : RegisterRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun registerUser(name: String, address: String,email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                } else {
                    val errorMsg = task.exception?.message ?: "Registration failed"
                    Log.e("register", errorMsg)
                }
            }
    }
}
