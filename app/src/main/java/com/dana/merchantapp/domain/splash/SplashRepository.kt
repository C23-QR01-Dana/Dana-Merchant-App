package com.dana.merchantapp.domain.splash

interface SplashRepository {
    fun checkIsLogin(callback: (Boolean) -> Unit)
}