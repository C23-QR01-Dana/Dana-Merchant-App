package com.dana.merchantapp.data.login

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class LoginRepositoryImpl: LoginRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun loginUser(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.v("login", "Login Success")
                    callback(true, "Login Success")
                } else {
                    Log.e("login", "wrong email or password")
                    val errorMsg = task.exception?.message ?: "Login failed"
                    callback(false, errorMsg)
                }
            }
    }
}