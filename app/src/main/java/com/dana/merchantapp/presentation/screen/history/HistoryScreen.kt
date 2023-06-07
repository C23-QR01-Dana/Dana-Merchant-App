package com.dana.merchantapp.presentation.screen.history

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.dana.merchantapp.presentation.model.Transaction

@Composable
fun HistoryScreen(historyViewModel: HistoryViewModel = hiltViewModel()) {
    historyViewModel.getTransactionsFromFirestore()
    TransactionHistory(transactions = historyViewModel.transactions.value, historyViewModel = historyViewModel)
}

@Composable
fun TransactionHistory(transactions: List<Transaction>?, historyViewModel: HistoryViewModel = hiltViewModel()) {
    val sortedTransactions = transactions?.sortedByDescending { it.timestamp }
    val transactionsByMonth = sortedTransactions?.filter { it.timestamp != null }?.groupBy { historyViewModel.convertTimestampToMonthYear(it.timestamp!!) }

    MaterialTheme {
        LazyColumn {
            transactionsByMonth?.forEach { (month, transactionsForMonth) ->
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
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, historyViewModel: HistoryViewModel = hiltViewModel()) {
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
        Text(text = "Date: ${transaction.timestamp?.let { historyViewModel.convertTimestampToFullDateTime(it) } ?: "N/A"}", style = MaterialTheme.typography.body2)
        Text(text = "Transaction Type: ${transaction.trxType ?: "N/A"}", style = MaterialTheme.typography.body2)
        Text(text = "Merchant ID: ${transaction.merchantId ?: "N/A"}", style = MaterialTheme.typography.body2)
        Text(text = "Payer ID: ${transaction.payerId ?: "N/A"}", style = MaterialTheme.typography.body2)
    }
}