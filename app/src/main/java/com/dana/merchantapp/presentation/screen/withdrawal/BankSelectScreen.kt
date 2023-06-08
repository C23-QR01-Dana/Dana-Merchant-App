package com.dana.merchantapp.presentation.screen.withdrawal

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dana.merchantapp.data.model.BankAccount
import kotlinx.coroutines.launch

@Composable
fun BankSelectScreen(navController: NavController, withdrawalViewModel: WithdrawalViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        withdrawalViewModel.getMerchantBank()
    }

    Column() {
        TopAppBar(
            title = { Text("Select Bank Account") },
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
        LazyColumn {
            item {
                AddBank(navController = navController)
            }
            item {
                Card(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                        .background(
                            color = Color.White
                        )
                ) { }
            }
            itemsIndexed(withdrawalViewModel.bankList.value){ _, item ->
                Bank(item, navController)
            }
        }
    }
}

@Composable
fun AddBank(navController: NavController) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentHeight()
            .clickable { navController.navigate("addBank") }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Add New Bank Account",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Composable
fun Bank(bankAccount: BankAccount, navController: NavController) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentHeight()
            .clickable { navController.navigate("withdrawAmount/${bankAccount.bankAccountNo}/${bankAccount.bankInst}") }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = bankAccount.accountName,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${bankAccount.bankInst} - ${bankAccount.bankAccountNo}",
                style = MaterialTheme.typography.caption,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}