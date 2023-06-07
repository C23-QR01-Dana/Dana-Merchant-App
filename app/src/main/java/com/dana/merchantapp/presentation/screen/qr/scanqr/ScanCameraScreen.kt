package com.dana.merchantapp.presentation.screen.qr.scanqr

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.dana.merchantapp.presentation.ui.component.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun ScanCameraScreen(navController: NavController, harga: Int) {

    val context = LocalContext.current
    val scannerView = remember { CodeScannerView(context) }
    val codeScanner = remember { CodeScanner(context, scannerView) }.apply {
        isAutoFocusEnabled = true
        scanMode = ScanMode.SINGLE
        decodeCallback = DecodeCallback { result ->
            // Callback when QR code is successfully scanned
            // Implement your UI changes here
        }
        errorCallback = ErrorCallback {
            // Callback when an error occurs while scanning QR code
            // Implement your UI changes here
        }
    }

    // Inisialisasi scanner saat komponen dijalankan
    LaunchedEffect(Unit) {
        codeScanner.startPreview()
    }

    // Callback saat QR code berhasil terdekripsi
    codeScanner.setDecodeCallback { result ->
        // Melakukan aksi yang berhubungan dengan UI di thread utama
        CoroutineScope(Dispatchers.Main).launch {
            val qrData = result.text.split("#")
            val isValid = qrData.size == 3 && qrData[0] == "DANA" && qrData[1] == "CPM"
            if (isValid) {
                Toast.makeText(context, "QR valid", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.ScanResult.createRoute("${harga}",qrData[2]))
            } else {
                Toast.makeText(context, "QR tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Izin kamera diberikan, mulai preview scanner
            codeScanner.startPreview()
        } else {
            // Izin kamera tidak diberikan, lakukan penanganan yang sesuai (misalnya menampilkan pesan kesalahan)
            Toast.makeText(context, "Izin kamera tidak diberikan", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        // Memeriksa izin kamera saat komponen dijalankan
        val isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!isCameraPermissionGranted) {
            // Jika izin kamera belum diberikan, minta izin
            permissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            // Jika izin kamera telah diberikan, mulai preview scanner
            codeScanner.startPreview()
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { scannerView }
    )
}

