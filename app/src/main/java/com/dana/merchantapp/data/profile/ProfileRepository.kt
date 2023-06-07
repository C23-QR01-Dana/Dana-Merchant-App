package com.dana.merchantapp.data.profile

import android.net.Uri
import com.dana.merchantapp.presentation.model.Merchant

interface ProfileRepository {
    fun getMerchant(callback: (Merchant?) -> Unit)
    fun updatePhoto(imageUri: Uri, callback: (Boolean) -> Unit)
    fun logoutUser(callback: (Boolean, String) -> Unit)
}