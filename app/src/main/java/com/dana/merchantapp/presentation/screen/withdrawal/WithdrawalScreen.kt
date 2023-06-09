package com.dana.merchantapp.presentation.screen.withdrawal

import android.icu.text.NumberFormat
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dana.merchantapp.data.model.MerchantWithdrawTransaction
import com.dana.merchantapp.data.model.PaymentTransaction
import com.dana.merchantapp.data.model.Transaction
import com.dana.merchantapp.presentation.screen.history.HistoryViewModel
import com.dana.merchantapp.presentation.screen.history.TransactionItem
import com.dana.merchantapp.presentation.ui.component.CustomTextField
import com.dana.merchantapp.presentation.ui.component.navigation.Screen
import com.dana.merchantapp.presentation.ui.theme.BluePrimary
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WithdrawalScreen(navController: NavController, withdrawalViewModel: WithdrawalViewModel = hiltViewModel()) {
    val transactions = withdrawalViewModel.transactions.value
    val sortedTransactions = transactions?.sortedByDescending { it.timestamp }
    val transactionsByDate = sortedTransactions?.filter { it.timestamp != null }?.groupBy { withdrawalViewModel.convertTimestampToDayMonthYear(it.timestamp!!) }

    LaunchedEffect(Unit) {
        withdrawalViewModel.getMerchant()
        withdrawalViewModel.getTransactions()
    }

    // Pull-to-refresh
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() = refreshScope.launch {
        refreshing = true
        withdrawalViewModel.getMerchant()
        withdrawalViewModel.getTransactions()
        refreshing = false
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

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
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),

                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFFFFFFF),
                        )
                    ) {
                        Text(text = "Withdraw", color = BluePrimary)
                }
            }
        }
    }
        Box(Modifier.pullRefresh(state)) {
            LazyColumn {
                transactionsByDate?.forEach { (month, transactionsForMonth) ->
                    item {
                        Text(
                            text = month,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.h6
                        )
                    }
                    items(transactionsForMonth.size) { index ->
                        val transaction = transactionsForMonth[index]
                        TrxItem(
                            transaction = transaction,
                        )
                    }
                    item {
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.LightGray
                        )
                    }
                }
            }
            PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
        }



    }
}

@Composable
fun TrxItem(transaction: Transaction, withdrawalViewModel: WithdrawalViewModel = hiltViewModel()) {
    var transactionTitle = "N/A"
    var transactionType = "N/A"
    var transactionAmount = "0"
    var transactionIcon = Icons.Default.QuestionMark
    var transactionSecondIcon = Icons.Default.QuestionMark
    var transactionSecondIconBackgroundColor = Color(0x00000000)
    val amountFormatter = NumberFormat.getNumberInstance(Locale.US)

    if (transaction is PaymentTransaction) {
        transactionTitle = "Customer Payment"
        transactionType = "Incoming"
        transactionAmount = "+ IDR ${amountFormatter.format(transaction.amount ?: 0)}"
        transactionIcon = Icons.Default.Group
        transactionSecondIcon = Icons.Default.SouthWest
        transactionSecondIconBackgroundColor = Color(0xFF00C853)
    } else if (transaction is MerchantWithdrawTransaction) {
        transactionTitle = "Withdraw to ${transaction.bankInst}"
        transactionType = "Outgoing"
        transactionAmount = "IDR ${amountFormatter.format(transaction.amount ?: 0)}"
        transactionIcon = Icons.Default.AccountBalance
        transactionSecondIcon = Icons.Default.NorthEast
        transactionSecondIconBackgroundColor = Color(0xFFE53935)
    }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(BluePrimary)
            ) {
                Icon(
                    imageVector = transactionIcon,
                    contentDescription = "Payment Group",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(22.dp)
                        .clip(CircleShape)
                )
            }
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(transactionSecondIconBackgroundColor)
            ) {
                Icon(
                    imageVector = transactionSecondIcon,
                    contentDescription = "Incoming/Outgoing Payment",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(18.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = transactionTitle,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = transaction.timestamp?.let { withdrawalViewModel.convertTimestampToHourMinute(it) } ?: "N/A",
                style = MaterialTheme.typography.caption,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            modifier = Modifier.padding(start = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = transactionAmount,
                style = if (transaction is PaymentTransaction) {
                    MaterialTheme.typography.subtitle1.copy(color = Color(0xFF00C853))
                } else {
                    MaterialTheme.typography.subtitle1
                }
            )
            Text(
                text = transactionType,
                style = MaterialTheme.typography.caption
            )
        }
    }
}