package com.dana.merchantapp.presentation.screen.withdrawal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dana.merchantapp.presentation.ui.component.CustomTextField
import com.dana.merchantapp.presentation.ui.component.navigation.Screen
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun WithdrawalScreen(navController: NavController, withdrawalViewModel: WithdrawalViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        withdrawalViewModel.getMerchant()
    }

    var amount by remember { mutableStateOf("") }

    Column(

    ) {
        Card(
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Box(
                modifier = Modifier
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
                        .padding(vertical = 16.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(
                        text = "Balance",
                        style = TextStyle(
                            fontSize = 25.sp,
                            textAlign = TextAlign.Left,
                            color = Color.White
                        ),
                    )
                    Text(
                        text = "Rp." + withdrawalViewModel.merchant.value?.balance.toString(),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 35.sp,
                            textAlign = TextAlign.Left,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Button(
                        onClick = {
                                  navController.navigate(Screen.BankSelect.route)
                        },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)

                    ) {
                        Text(text = "Withdraw")
                    }
                }
            }
        }
    }

//    Column(
//        modifier = Modifier
//            .fillMaxSize(),
//        verticalArrangement = Arrangement.Top,
//    ) {
//        Card (
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight()
//                .padding(16.dp)
//                .shadow(8.dp, shape = RoundedCornerShape(8.dp)),
//            shape = RoundedCornerShape(8.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(16.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .padding(16.dp),
//                ) {
//                    Text(
//                        text = "Balance: ",
//                        style = TextStyle(
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 30.sp,
//                            textAlign = TextAlign.Center
//                        ),
//                        modifier = Modifier
//                            .padding(bottom = 4.dp)
//                    )
//                    Text(
//                        text = ("RP." + withdrawalViewModel.merchant.value?.balance.toString())
//                            ?: "0",
//                        style = TextStyle(
//                            fontSize = 30.sp,
//                            textAlign = TextAlign.Center
//                        ),
//                        modifier = Modifier
//                    )
//                }
//                CustomTextField(
//                    value = amount,
//                    onValueChange = { amount = it },
//                    label = "value",
//                    keyboardType = KeyboardType.Text
//                )
//            }
//        }
//    }
}