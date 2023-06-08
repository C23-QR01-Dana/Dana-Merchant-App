package com.dana.merchantapp.domain.profile

import android.net.Uri
import com.dana.merchantapp.data.model.Merchant

interface ProfileRepository {
    fun getMerchant(callback: (Merchant?) -> Unit)
    fun updatePhoto(imageUri: Uri, callback: (Boolean) -> Unit)
    fun logoutUser(callback: (Boolean, String) -> Unit)
}