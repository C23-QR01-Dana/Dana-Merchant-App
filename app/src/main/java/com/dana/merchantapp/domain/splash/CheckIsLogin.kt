package com.dana.merchantapp.domain.splash

import com.dana.merchantapp.domain.register.RegisterRepository
import javax.inject.Inject

class CheckIsLogin @Inject constructor (private val splashRepository: SplashRepository) {
    fun checkIsLogin(callback: (Boolean) -> Unit) {
        splashRepository.checkIsLogin { isLogin ->
            callback(isLogin)
        }
    }
}
