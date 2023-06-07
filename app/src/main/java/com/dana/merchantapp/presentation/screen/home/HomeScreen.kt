package com.dana.merchantapp.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dana.merchantapp.presentation.main.MainViewModel

@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    // Text("Home")

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
                        text = "Hello, Nama Merchant!",
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
                        text = "Rp. 100.000",
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
    }

}