package com.dana.merchantapp.presentation.screen.withdrawal

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.dana.merchantapp.data.model.BankAccount
import com.dana.merchantapp.presentation.ui.component.CustomTextField
import com.dana.merchantapp.presentation.ui.component.navigation.Screen
import com.dana.merchantapp.presentation.ui.theme.BluePrimary

@Composable
fun WithdrawAmountScreen (navController: NavController, accountNo: String, bankInst: String, withdrawalViewModel: WithdrawalViewModel = hiltViewModel()) {
    var amount by remember { mutableStateOf("") }
    var isInvalid by remember { mutableStateOf(false) }
    var isComplete by remember { mutableStateOf(false) }
    val isWithdrawSuccess by withdrawalViewModel.isWithdrawSuccess.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        withdrawalViewModel.getMerchant()
    }

    if (isWithdrawSuccess && !isComplete) {
        Log.v("WithdrawAmountScreen", "Withdraw Success")
        isComplete = true
        Toast.makeText(context, "Withdrawal Success", Toast.LENGTH_SHORT).show()
        navController.navigate(Screen.Withdrawal.route) {
            popUpTo(Screen.Withdrawal.route) {
                inclusive = true
            }
        }
    }

    Column(
    ) {
        TopAppBar(
            title = { Text("Withdraw Money") },
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
        Text(
            text = "$bankInst - $accountNo",
            style = TextStyle(
                color = Color.Black,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
        Box(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
            CustomTextField(
                value = amount,
                onValueChange = {
                    amount = it
                },
                label = "amount",
                isError = isInvalid,
                keyboardType = KeyboardType.Number,
            )
        }
        Text(
            text = "Balance: Rp.${withdrawalViewModel.merchant.value?.balance}",
            style = TextStyle(
                color = Color.Black,
                fontSize = 15.sp,
            ),
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
        )
        Button(
            onClick = {
                val amountInt = amount.toLongOrNull()
                if (amountInt != null && withdrawalViewModel.merchant.value != null) {
                    if (withdrawalViewModel.merchant.value!!.balance < amountInt) {
                        isInvalid = true
                        Toast.makeText(
                            context,
                            "Amount Must Be Below Rp.${withdrawalViewModel.merchant.value?.balance}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (amountInt == 0L){
                        isInvalid = true
                        Toast.makeText(
                            context,
                            "Amount Must Be Above Rp.0",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        isInvalid = false
                        withdrawalViewModel.withdraw(amountInt, System.currentTimeMillis(), BankAccount("", accountNo, bankInst))
                    }
                } else {
                    isInvalid = true
                    Toast.makeText(
                        context,
                        "Amount Must Be Filled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = "Withdraw",
            )
        }
    }
}