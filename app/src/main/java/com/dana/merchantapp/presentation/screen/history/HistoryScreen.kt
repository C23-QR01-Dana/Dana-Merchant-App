package com.dana.merchantapp.presentation.screen.history

import android.icu.text.NumberFormat
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.dana.merchantapp.data.model.MerchantWithdrawTransaction
import com.dana.merchantapp.data.model.PaymentTransaction
import com.dana.merchantapp.data.model.Transaction
import com.dana.merchantapp.presentation.ui.theme.BluePrimary
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun HistoryScreen(historyViewModel: HistoryViewModel = hiltViewModel()) {
    historyViewModel.getTransactions()
    TransactionHistory(transactions = historyViewModel.transactions.value, historyViewModel = historyViewModel)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransactionHistory(transactions: List<Transaction>?, historyViewModel: HistoryViewModel = hiltViewModel()) {
    val sortedTransactions = transactions?.sortedByDescending { it.timestamp }
    val transactionsByDate = sortedTransactions?.filter { it.timestamp != null }?.groupBy { historyViewModel.convertTimestampToDayMonthYear(it.timestamp!!) }

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        historyViewModel.getTransactions()
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)

    MaterialTheme {
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
                        TransactionItem(
                            transaction = transaction,
                            historyViewModel = historyViewModel
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
fun TransactionItem(transaction: Transaction, historyViewModel: HistoryViewModel = hiltViewModel()) {
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
                    contentDescription = "Incoming Payment",
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
                    contentDescription = "Incoming Payment",
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
                text = transaction.timestamp?.let { historyViewModel.convertTimestampToHourMinute(it) } ?: "N/A",
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

