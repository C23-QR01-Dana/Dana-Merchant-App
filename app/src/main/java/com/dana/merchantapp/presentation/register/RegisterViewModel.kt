package com.dana.merchantapp.presentation.register



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dana.merchantapp.domain.register.RegisterUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor (private val registerUser: RegisterUser) : ViewModel() {
    private val _registerResult: MutableLiveData<Boolean> = MutableLiveData()
    val registerResult: LiveData<Boolean> = _registerResult
    private val _registerMessage: MutableLiveData<String> = MutableLiveData()
    val registerMessage: LiveData<String> = _registerMessage
    fun registerUser(name: String, address: String, email: String, password: String) {
        // Panggil metode registerUser di use case
        registerUser.registerUser(name, address, email, password) { isSuccess, message ->
            _registerResult.value = isSuccess
            _registerMessage.value = message
        }
    }


}


