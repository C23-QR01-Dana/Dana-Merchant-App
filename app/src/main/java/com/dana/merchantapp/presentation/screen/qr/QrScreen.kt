package com.dana.merchantapp.presentation.screen.qr


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.dana.merchantapp.presentation.screen.qr.dynamicqr.DynamicQrScreen
import com.dana.merchantapp.presentation.screen.qr.scanqr.ScanQrScreen
import com.dana.merchantapp.presentation.screen.qr.staticqr.StaticQrScreen
import com.dana.merchantapp.presentation.ui.component.CustomTabLayout


@Composable
fun QrScreen(navController: NavController, initialTabIndex: Int) {
    val tabs = listOf("Static QR", "Scan QR", "Dynamic QR")
    val selectedTabIndex = remember { mutableStateOf(initialTabIndex)}

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        CustomTabLayout(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex.value,
            onTabSelected = { index -> selectedTabIndex.value = index }
        )

        when (selectedTabIndex.value) {
            0 -> StaticQrScreen()
            1 -> ScanQrScreen(navController)
            2 -> DynamicQrScreen()
        }
    }
}


