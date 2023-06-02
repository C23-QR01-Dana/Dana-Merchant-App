package com.dana.merchantapp.data.main

import com.google.firebase.auth.FirebaseAuth

class MainRepositoryImpl: MainRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun logoutUser(callback: (Boolean, String) -> Unit) {
        auth.signOut()
        callback(true, "Logout Success")
    }
}