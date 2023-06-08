package com.dana.merchantapp.domain.profile

import com.dana.merchantapp.domain.profile.ProfileRepository
import javax.inject.Inject

class LogoutUser @Inject constructor (private val profileRepository: ProfileRepository){
    fun logoutUser(callback: (Boolean, String) -> Unit) {
        profileRepository.logoutUser { isSuccess, message ->
            callback(isSuccess, message)
        }
    }
}