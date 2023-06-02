package com.dana.merchantapp.presentation.screen.qr

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dana.merchantapp.data.qr.QRRepositoryImpl
import com.dana.merchantapp.domain.qr.QRUseCase
import com.dana.merchantapp.presentation.screen.qr.staticqr.StaticQrScreen
import com.dana.merchantapp.presentation.ui.component.CustomTabLayout

@Composable
fun QrScreen() {
    val qrRepository = QRRepositoryImpl()
    val qrUseCase = QRUseCase(qrRepository)

    val tabs = listOf("Static QR", "Scan QR", "Dynamic QR")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        CustomTabLayout(
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { index -> selectedTabIndex = index }
        )

        when (selectedTabIndex) {
            0 -> StaticQrScreen(qrUseCase)
            1 -> ScanQrScreen()
            2 -> DynamicQrScreen()
        }
    }
}

@Composable
fun DynamicQrScreen() {
    Text("Dynamic QR")
}

@Composable
fun ScanQrScreen() {
    Text("Scan QR")
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Aksi yang ingin Anda lakukan setelah pemindaian berhasil dilakukan
            val scannedData = result.data?.getStringExtra("scanned_data")
            Toast.makeText(context, "Hasil scan: $scannedData", Toast.LENGTH_SHORT).show()
        }
    }

    Button(
        onClick = {

        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)

    ) {
        Text(text = "Scan QR")
    }
}