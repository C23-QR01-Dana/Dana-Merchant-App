package com.dana.merchantapp.data.login

import android.util.Log
import com.dana.merchantapp.domain.login.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor (private val auth: FirebaseAuth): LoginRepository {
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