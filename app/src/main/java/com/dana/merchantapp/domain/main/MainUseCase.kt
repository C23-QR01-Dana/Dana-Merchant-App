package com.dana.merchantapp.domain.main

import com.dana.merchantapp.data.main.MainRepository

class MainUseCase (private val mainRepository: MainRepository) {
    fun logoutUser(callback: (Boolean, String) -> Unit) {
        mainRepository.logoutUser { isSuccess, message ->
            callback(isSuccess, message)
        }
    }
}