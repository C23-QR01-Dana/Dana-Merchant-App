package com.dana.merchantapp.presentation.screen.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TransactionItemDetailsScreen(
    navController: NavController,
    transactionTitle: String,
    transactionAmount: String,
    transactionId: String,
    merchantId: String,
    transactionTimestamp: String,
    transactionType: String,
    transactionPartyId: String
) {
    Column {
        TopAppBar(
            title = { Text("Transaction Item Details") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            },
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF86B0FF), Color(0xFF408CE2)),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent, shape = RoundedCornerShape(8.dp))
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Column {
                Text(transactionTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold)

            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        ) {
            TransactionDetailRow(label = "Amount", value = transactionAmount, 16)
            GrayLineSeparator()
            TransactionDetailRow(label = "Transaction Type", value = transactionType, 14)
            TransactionDetailRow(label = "Transaction Time", value = transactionTimestamp, 14)
            TransactionDetailRow(label = "Transaction ID", value = transactionId, 14)
            TransactionDetailRow(label = "Other Party ID", value = transactionPartyId, 14)
        }
    }
}

@Composable
fun TransactionDetailRow(label: String, value: String, valueSize: Int) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 12.sp, modifier = Modifier.weight(1f), color = Color(0x88000000))
        Text(value, fontSize = valueSize.sp, textAlign = TextAlign.End, modifier = Modifier.weight(4f))
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun GrayLineSeparator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.LightGray)
    )
    Spacer(modifier = Modifier.height(12.dp))
}

