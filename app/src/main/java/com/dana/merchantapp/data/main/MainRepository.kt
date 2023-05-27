package com.dana.merchantapp.data.main

interface MainRepository {
    fun logoutUser(callback: (Boolean, String) -> Unit)
}