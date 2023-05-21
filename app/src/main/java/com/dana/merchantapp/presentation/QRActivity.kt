package com.dana.merchantapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.dana.merchantapp.presentation.ui.component.CustomBottomNavigation
import com.dana.merchantapp.presentation.ui.component.CustomTabLayout
import com.dana.merchantapp.presentation.ui.theme.BlueButton
import com.dana.merchantapp.presentation.ui.theme.MerchantAppTheme

class QRActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MerchantAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    QRScreen()
                }
            }
        }
    }
}



@Composable
fun QRScreen() {
    Scaffold(
        bottomBar = {
            CustomBottomNavigation()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Handle FAB click event */ },
                backgroundColor = BlueButton

            ) {
                Icon(Icons.Default.QrCode, contentDescription = "QR", tint = Color.White)
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        // Content area
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ){
            CustomTabLayout()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun QRScreenPreview() {
    MerchantAppTheme {
        QRScreen()
    }
}