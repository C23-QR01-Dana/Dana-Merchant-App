package com.dana.merchantapp.presentation.screen.qr.dynamicqr

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dana.merchantapp.presentation.ui.component.CustomTextField
import com.dana.merchantapp.presentation.ui.component.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun DynamicQrScreen(dynamicQrViewModel: DynamicQrViewModel = hiltViewModel()) {
    var harga by remember { mutableStateOf("") }
    var isHargaEmpty by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val qrCodeBitmap = dynamicQrViewModel.qrCodeBitmap.value

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

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                qrCodeBitmap?.asImageBitmap()?.let { bitmap ->
                    Image(
                        bitmap = bitmap,
                        contentDescription = "QR Code",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(300.dp)
                            .padding(bottom = 16.dp)
                    )
                }
            }
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
                dynamicQrViewModel.generateDynamicQR(harga)
                dynamicQrViewModel.getMerchant()
            }

        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

    ) {
        Text(text = "Generate QR")
    }
}