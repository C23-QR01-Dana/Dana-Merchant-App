package com.dana.merchantapp.presentation.screen.qr

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dana.merchantapp.data.qr.QRRepositoryImpl
import com.dana.merchantapp.domain.qr.QRUseCase
import com.dana.merchantapp.presentation.QRActivity
import com.dana.merchantapp.presentation.screen.qr.staticqr.StaticQrScreen
import com.dana.merchantapp.presentation.ui.component.CustomTabLayout
import com.dana.merchantapp.presentation.ui.component.CustomTextField

@Composable
fun QrScreen() {

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
            0 -> StaticQrScreen()
            1 -> ScanQrScreen()
            2 -> DynamicQrScreen()
        }
    }
}

@Composable
fun DynamicQrScreen() {
//    Text("Dynamic QR")
    var harga by remember { mutableStateOf("") }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

        ) {
            CustomTextField(
                value = harga,
                onValueChange = { harga = it },
                label = "value",
                keyboardType = KeyboardType.Text
            )
        }

    }

    Button(
        onClick = {
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

    ) {
        Text(text = "Generate QR")
    }
}

@Composable
fun ScanQrScreen() {
//    Text("Scan QR")
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
    var harga by remember { mutableStateOf("") }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

        ) {
            CustomTextField(
                value = harga,
                onValueChange = { harga = it },
                label = "value",
                keyboardType = KeyboardType.Text
            )
        }

    }

    Button(
        onClick = {
            // permissionLauncher.launch(Intent(context, QRActivity::class.java))
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

    ) {
        Text(text = "Scan QR")
    }
}