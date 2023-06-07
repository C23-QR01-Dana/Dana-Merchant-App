package com.dana.merchantapp.presentation.screen.profile

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dana.merchantapp.domain.main.MainUseCase
import com.dana.merchantapp.domain.profile.ProfileUseCase
import com.dana.merchantapp.presentation.model.Merchant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileUseCase: ProfileUseCase) : ViewModel() {
    private val _merchant = mutableStateOf<Merchant?>(null)
    val merchant: State<Merchant?> get() = _merchant
    private val _logoutResult: MutableLiveData<Boolean> = MutableLiveData()
    val logoutResult: LiveData<Boolean> = _logoutResult
    private val _logoutMessage: MutableLiveData<String> = MutableLiveData()
    val logoutMessage: LiveData<String> = _logoutMessage

    fun getMerchant() {
        profileUseCase.getMerchant { merchant ->
            if (merchant != null) {
                _merchant.value = merchant
            } else {
                _merchant.value = null
            }
        }
    }

    fun updatePhoto(imageUri: Uri) {
        profileUseCase.updatePhoto(imageUri) { isSuccess ->
            if (isSuccess) {
                getMerchant()
            }
        }
    }

    fun logoutUser() {
        profileUseCase.logoutUser { isSuccess, message ->
            _logoutMessage.value = message
            _logoutResult.value = isSuccess
        }
    }
}