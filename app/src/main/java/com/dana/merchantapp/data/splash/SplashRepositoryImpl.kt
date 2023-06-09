package com.dana.merchantapp.data.splash

import com.dana.merchantapp.domain.splash.SplashRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SplashRepositoryImpl  @Inject constructor (private val auth: FirebaseAuth) : SplashRepository {
    override fun checkIsLogin(callback: (Boolean) -> Unit) {
        if(auth.currentUser != null){
            callback(true)
        }
        else{
            callback(false)
        }
    }
}