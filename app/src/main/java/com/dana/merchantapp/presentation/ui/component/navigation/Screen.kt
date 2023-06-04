package com.dana.merchantapp.presentation.ui.component.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object History : Screen("history")
    object Withdrawal : Screen("withdrawal")
    object Profile : Screen("profile")
    object QR : Screen("qr")
}