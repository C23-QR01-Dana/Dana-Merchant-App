package com.dana.merchantapp.presentation.screen.qr.scanqr


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dana.merchantapp.presentation.ui.component.CustomTextField
import com.dana.merchantapp.presentation.ui.component.navigation.Screen
import kotlinx.coroutines.launch


@Composable
fun ScanQrScreen(navController: NavController) {
    var harga by remember { mutableStateOf("") }
    var isHargaEmpty by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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
                isError = isHargaEmpty,
                keyboardType = KeyboardType.Number
            )

        }

    }

    Button(
        onClick = {
            val hargaInt = harga.toIntOrNull()
            if (hargaInt == null) {
                isHargaEmpty = true
                coroutineScope.launch {
                    Toast.makeText(context, "Harga harus diisi dengan benar", Toast.LENGTH_SHORT).show()
                }

            } else {
                isHargaEmpty = false
                navController.navigate(Screen.ScanCamera.createRoute(harga))
            }
        },
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

    ) {
        Text(text = "Scan QR")
    }
}