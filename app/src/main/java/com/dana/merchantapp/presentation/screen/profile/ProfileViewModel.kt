package com.dana.merchantapp.presentation.screen.profile

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dana.merchantapp.domain.profile.UpdatePhoto
import com.dana.merchantapp.data.model.Merchant
import com.dana.merchantapp.domain.home.GetMerchant
import com.dana.merchantapp.domain.profile.LogoutUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updatePhoto: UpdatePhoto,
    private val getMerchant: GetMerchant,
    private val logoutUser: LogoutUser
    ) : ViewModel() {

    private val _merchant = mutableStateOf<Merchant?>(null)
    val merchant: State<Merchant?> get() = _merchant
    private val _logoutResult = mutableStateOf<Boolean>(false)
    val logoutResult: State<Boolean> get() = _logoutResult
    private val _isUploading = mutableStateOf<Boolean>(false)
    val isUploading: State<Boolean> get() = _isUploading

    fun getMerchant() {
        getMerchant.getMerchant { merchant ->
            if (merchant != null) {
                _merchant.value = merchant
            } else {
                _merchant.value = null
            }
        }
    }

    fun updatePhoto(imageUri: Uri) {
        _isUploading.value = true
        updatePhoto.updatePhoto(imageUri) { isSuccess ->
            if (isSuccess) {
                _isUploading.value = false
                getMerchant()
            }
        }
    }

    fun logoutUser() {
        logoutUser.logoutUser { isSuccess, message ->
            _logoutResult.value = isSuccess
        }
    }
}