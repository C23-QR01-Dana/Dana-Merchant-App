package com.dana.merchantapp.domain.profile

import android.net.Uri
import com.dana.merchantapp.data.profile.ProfileRepository
import com.dana.merchantapp.presentation.model.Merchant
import javax.inject.Inject

class ProfileUseCase @Inject constructor (private val profileRepository: ProfileRepository) {
    fun getMerchant(callback: (Merchant?) -> Unit){
        profileRepository.getMerchant  { merchant ->
            callback(merchant)
        }
    }

    fun updatePhoto(imageUri: Uri, callback: (Boolean) -> Unit) {
        profileRepository.updatePhoto(imageUri) { isSuccess ->
            callback(isSuccess)
        }
    }

    fun logoutUser(callback: (Boolean, String) -> Unit) {
        profileRepository.logoutUser { isSuccess, message ->
            callback(isSuccess, message)
        }
    }
}