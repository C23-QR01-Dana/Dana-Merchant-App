package com.dana.merchantapp.presentation.screen.home


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dana.merchantapp.presentation.ui.component.navigation.Screen
import com.dana.merchantapp.presentation.ui.theme.BluePrimary

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {
    // Text("Home")
    LaunchedEffect(Unit) {
        homeViewModel.getMerchant()
    }

    Column(){
        Card(
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF86B0FF), Color(0xFF408CE2)),
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 16.dp, horizontal = 24.dp),
                    verticalArrangement = Arrangement.Center,

                ) {
                    Text(
                        text = "Hello, ${homeViewModel.merchant.value?.merchantName ?: ""}",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Left,
                            color = Color.White
                        ),
                    )
                    Text(
                        text = "Balance:",
                        style = TextStyle(
                            fontWeight = FontWeight.Light,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Left,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = "Rp.${homeViewModel.merchant.value?.balance ?: ""}",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Left,
                            color = Color.White
                        ),

                    )
                }
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top=16.dp, start = 8.dp,end=8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ShortcutCard(
                title = "Static",
                icon = Icons.Default.QrCode,
                onClick = {
                    navController.navigate(Screen.QR.createRoute(0))
                }
            )
            ShortcutCard(
                title = "Scan",
                icon = Icons.Default.QrCodeScanner,
                onClick = {
                    navController.navigate(Screen.QR.createRoute(1))
                }
            )
            ShortcutCard(
                title = "Dynamic",
                icon = Icons.Default.QrCode2,
                onClick = {
                    navController.navigate(Screen.QR.createRoute(2))
                }
            )
        }
    }

}

@Composable
fun ShortcutCard(title: String,icon:ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(95.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Shortcut Icon",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = title,
                    style = TextStyle(color = BluePrimary, fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}




