package com.dana.merchantapp.presentation.screen.history

import android.icu.text.NumberFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.dana.merchantapp.presentation.model.MerchantWithdrawTransaction
import com.dana.merchantapp.presentation.model.PaymentTransaction
import com.dana.merchantapp.presentation.model.Transaction
import com.dana.merchantapp.presentation.ui.theme.BluePrimary
import java.util.*

@Composable
fun HistoryScreen(historyViewModel: HistoryViewModel = hiltViewModel()) {
    historyViewModel.getTransactionsFromFirestore()
    TransactionHistory(transactions = historyViewModel.transactions.value, historyViewModel = historyViewModel)
}

@Composable
fun TransactionHistory(transactions: List<Transaction>?, historyViewModel: HistoryViewModel = hiltViewModel()) {
    val sortedTransactions = transactions?.sortedByDescending { it.timestamp }
    val transactionsByDate = sortedTransactions?.filter { it.timestamp != null }?.groupBy { historyViewModel.convertTimestampToDayMonthYear(it.timestamp!!) }

    MaterialTheme {
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
                    TransactionItem(transaction = transaction, historyViewModel = historyViewModel)
                }
                item {
                    Divider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, historyViewModel: HistoryViewModel = hiltViewModel()) {
    var transactionTitle = "N/A"
    var transactionType = "N/A"
    var transactionAmount = "0"
    val amountFormatter = NumberFormat.getNumberInstance(Locale.US)

    if (transaction is PaymentTransaction) {
        transactionTitle = "Customer Payment"
        transactionType = "Incoming"
        transactionAmount = "+ IDR ${amountFormatter.format(transaction.amount ?: 0)}"
    } else if (transaction is MerchantWithdrawTransaction) {
        transactionTitle = "Withdraw to ${transaction.bankInst}"
        transactionType = "Outgoing"
        transactionAmount = "IDR ${amountFormatter.format(transaction.amount ?: 0)}"
    }



    Row(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        Image(
//            painter = rememberImagePainter(data = "https://seeklogo.com/images/V/valorant-logo-FAB2CA0E55-seeklogo.com.png"),
//            contentDescription = "Transaction Image",
//            modifier = Modifier
//                .size(48.dp)
//                .clip(CircleShape)
//        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(BluePrimary)
        ) {
            Icon(
                imageVector = Icons.Default.SouthWest,
                contentDescription = "Incoming Payment",
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
            )
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


@Composable
fun OldTransactionItem(transaction: Transaction, historyViewModel: HistoryViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Amount: Rp${transaction.amount ?: 0}", style = MaterialTheme.typography.body1)
        Spacer(Modifier.height(4.dp))
        Text(text = "Date: ${transaction.timestamp?.let { historyViewModel.convertTimestampToHourMinute(it) } ?: "N/A"}", style = MaterialTheme.typography.body2)
        Text(text = "Transaction Type: ${transaction.trxType ?: "N/A"}", style = MaterialTheme.typography.body2)
        Text(text = "Merchant ID: ${transaction.merchantId ?: "N/A"}", style = MaterialTheme.typography.body2)

        // Handle specific attributes for each transaction type
        when (transaction) {
            is PaymentTransaction -> {
                Text(text = "Payer ID: ${transaction.payerId ?: "N/A"}", style = MaterialTheme.typography.body2)
            }
            is MerchantWithdrawTransaction -> {
                Text(text = "Bank Account No: ${transaction.bankAccountNo ?: "N/A"}", style = MaterialTheme.typography.body2)
                Text(text = "Bank Institution: ${transaction.bankInst ?: "N/A"}", style = MaterialTheme.typography.body2)
            }
        }
    }
}

