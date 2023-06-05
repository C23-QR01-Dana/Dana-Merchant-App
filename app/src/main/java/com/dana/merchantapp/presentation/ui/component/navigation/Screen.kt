package com.dana.merchantapp.presentation.ui.component.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object History : Screen("history")
    object Withdrawal : Screen("withdrawal")
    object Profile : Screen("profile")
    object QR : Screen("qr")
    object ScanCamera : Screen("scanCamera/{harga}") {
        fun createRoute(harga: String) = "scanCamera/$harga"
    }
    object ScanResult : Screen("scanResult/{harga}/{userId}") {
        fun createRoute(harga: String, userId: String) = "scanResult/$harga/$userId"
    }

}