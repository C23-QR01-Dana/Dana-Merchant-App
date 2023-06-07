package com.dana.merchantapp.domain.profile

import android.net.Uri
import javax.inject.Inject

class UpdatePhoto @Inject constructor (private val profileRepository: ProfileRepository) {
    fun updatePhoto(imageUri: Uri, callback: (Boolean) -> Unit) {
        profileRepository.updatePhoto(imageUri) { isSuccess ->
            callback(isSuccess)
        }
    }
}