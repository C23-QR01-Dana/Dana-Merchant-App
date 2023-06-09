package com.dana.merchantapp.presentation.screen.qr.staticqr

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun StaticQrScreen(staticQrViewModel: StaticQrViewModel = hiltViewModel()) {

    // Generate Static QR Code
    LaunchedEffect(Unit) {
        staticQrViewModel.generateStaticQR()
        staticQrViewModel.getMerchant()
    }

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
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Tampilkan teks merchantName di atas QR
            Text(
                text = staticQrViewModel.merchant.value?.merchantName ?: "",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(top = 16.dp)
            )

            staticQrViewModel.qrCodeBitmap.value?.asImageBitmap()?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = "QR Code",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(300.dp)
                )
            }

        }

    }


}