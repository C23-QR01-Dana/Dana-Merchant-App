package com.dana.merchantapp.presentation.screen.history

import android.icu.text.NumberFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dana.merchantapp.data.model.MerchantWithdrawTransaction
import com.dana.merchantapp.data.model.PaymentTransaction
import com.dana.merchantapp.data.model.Transaction
import com.dana.merchantapp.presentation.ui.component.navigation.Screen
import com.dana.merchantapp.presentation.ui.theme.BluePrimary
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun HistoryScreen(navController: NavController, historyViewModel: HistoryViewModel = hiltViewModel()) {
    historyViewModel.getTransactions()
    TransactionHistory(navController = navController, transactions = historyViewModel.transactions.value)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransactionHistory(navController: NavController, transactions: List<Transaction>?, historyViewModel: HistoryViewModel = hiltViewModel()) {
    val sortedTransactions = transactions?.sortedByDescending { it.timestamp }
    val transactionsByDate = sortedTransactions?.filter { it.timestamp != null }?.groupBy { historyViewModel.convertTimestampToDayMonthYear(it.timestamp!!) }

    // Pull-to-refresh
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() = refreshScope.launch {
        refreshing = true
        historyViewModel.getTransactions()
        refreshing = false
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    // Filter bottom sheet
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    // Amount filter
    val fromAmount = remember { mutableStateOf("") }
    val fromAmountError = remember { mutableStateOf("") }
    val toAmount = remember { mutableStateOf("") }
    val toAmountError = remember { mutableStateOf("") }
    val selectedTransactionType = remember { mutableStateOf("All") }
    val isFilterApplied = remember { mutableStateOf(false) }

    MaterialTheme {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetElevation = 100.dp,
            sheetContent = {
                Column(Modifier.padding(16.dp)) {
                    Box(
                        Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(Color.Gray, RoundedCornerShape(2.dp))
                            .align(Alignment.CenterHorizontally)
                    )
                    Text("Amount")
                    Row(Modifier.padding(vertical = 8.dp)) {
                        TextField(
                            value = fromAmount.value,
                            onValueChange = { newValue ->
                                if (newValue.isNotEmpty() && newValue.isDigitsOnly() && newValue.toLong() <= 10000000000) {
                                    fromAmount.value = newValue
                                    fromAmountError.value = ""
                                    // Check error for To amount
                                    if (toAmount.value.isEmpty()) {
                                        toAmountError.value = "Must be filled"
                                    } else {
                                        val toAmountValue = toAmount.value.toLong()
                                        if (toAmountValue <= newValue.toLong()) {
                                            toAmountError.value = "Amount must be greater"
                                        } else {
                                            toAmountError.value = ""
                                        }
                                    }
                                } else if (newValue.isEmpty()) {
                                    fromAmount.value = ""
                                    toAmountError.value = ""
                                    if (toAmount.value.isNotEmpty()) {
                                        fromAmountError.value = "Must be filled"
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White, RoundedCornerShape(4.dp)),
                            placeholder = { Text("From") },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White,
                            ),
                            isError = fromAmountError.value.isNotEmpty(),
                            visualTransformation = ThousandSeparatorTransformation()

                        )
                        Spacer(Modifier.width(8.dp))
                        TextField(
                            value = toAmount.value,
                            onValueChange = { newValue ->
                                if (newValue.isNotEmpty() && newValue.isDigitsOnly() && newValue.toLong() <= 10000000000) {
                                    toAmount.value = newValue

                                    if (fromAmount.value.isNotEmpty()) {
                                        val fromAmountValue = fromAmount.value.toLong()
                                        val toAmountValue = newValue.toLong()
                                        if (toAmountValue <= fromAmountValue) {
                                            toAmountError.value = "Amount must be greater"
                                        } else {
                                            toAmountError.value = ""
                                        }
                                    } else {
                                        fromAmountError.value = "Must be filled"
                                    }
                                } else if (newValue.isEmpty()) {
                                    toAmount.value = ""
                                    fromAmountError.value = ""
                                    if (fromAmount.value.isNotEmpty()) {
                                        toAmountError.value = "Must be filled"
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White, RoundedCornerShape(4.dp)),
                            placeholder = { Text("To") },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White,
                            ),
                            isError = toAmountError.value.isNotEmpty(),
                            visualTransformation = ThousandSeparatorTransformation()
                        )
                    }
                    Text("Type", modifier = Modifier.padding(top = 16.dp))
                    Row(Modifier.padding(vertical = 8.dp)) {
                        RadioButton(
                            selected = selectedTransactionType.value == "All",
                            onClick = { selectedTransactionType.value = "All" }
                        )
                        Text(
                            "All",
                            modifier = Modifier.padding(start = 8.dp)
                                .align(Alignment.CenterVertically)
                        )

                        Spacer(Modifier.width(16.dp))

                        RadioButton(
                            selected = selectedTransactionType.value == "Incoming",
                            onClick = { selectedTransactionType.value = "Incoming" }
                        )
                        Text(
                            "Incoming",
                            modifier = Modifier.padding(start = 8.dp)
                                .align(Alignment.CenterVertically)
                        )

                        Spacer(Modifier.width(16.dp))

                        RadioButton(
                            selected = selectedTransactionType.value == "Outgoing",
                            onClick = { selectedTransactionType.value = "Outgoing" }
                        )
                        Text(
                            "Outgoing",
                            modifier = Modifier.padding(start = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                    Button(
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            // Apply filters
                            scope.launch {
                                sheetState.hide()
                                if (fromAmount.value.isEmpty() && toAmount.value.isEmpty()) {
                                    historyViewModel.minAmount.value = 0L
                                    historyViewModel.maxAmount.value = Long.MAX_VALUE
                                } else {
                                    historyViewModel.minAmount.value = fromAmount.value.toLong()
                                    historyViewModel.maxAmount.value = toAmount.value.toLong()
                                }
                                historyViewModel.transactionType.value =
                                    selectedTransactionType.value
                                historyViewModel.getTransactions()
                                isFilterApplied.value = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = BluePrimary,
                        ),
                        enabled = fromAmountError.value.isEmpty() && toAmountError.value.isEmpty()
                    ) {
                        Text("APPLY")
                    }
                    Button(
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            // Reset filters
                            scope.launch {
                                sheetState.hide()
                                fromAmount.value = ""
                                toAmount.value = ""
                                fromAmountError.value = ""
                                toAmountError.value = ""
                                selectedTransactionType.value = "All"
                                historyViewModel.minAmount.value = 0L
                                historyViewModel.maxAmount.value = Long.MAX_VALUE
                                historyViewModel.transactionType.value = "All"
                                historyViewModel.getTransactions()
                                isFilterApplied.value = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            ,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFFFFFFF),
                        )
                    ) {
                        Text(text = "RESET FILTER", color = BluePrimary)
                    }
                    Box(Modifier.padding(bottom = 10.dp))
                }
            },
            content = {
                Column {
//                    TopAppBar(
//                        backgroundColor = BluePrimary,
//                        title = {
//                            Text(
//                                text = "Transaction History" + historyViewModel.recentIncome.value + " " + historyViewModel.recentOutcome.value,
//                                style = MaterialTheme.typography.h6,
//                                fontWeight = FontWeight.Bold
//                            )
//                        },
//                        modifier = Modifier
//                            .background(
//                                brush = Brush.linearGradient(
//                                    colors = listOf(Color(0xFF86B0FF), Color(0xFF408CE2)),
//                                    start = Offset.Zero,
//                                    end = Offset.Infinite
//                                )
//                            ),
//                        actions = {
//                            IconButton(onClick = { scope.launch { sheetState.show() } }) {
//                                Icon(Icons.Filled.FilterList, contentDescription = "Filter")
//                                if (isFilterApplied.value) {
//                                    Box(
//                                        Modifier
//                                            .padding(start = 20.dp, bottom = 20.dp)
//                                            .size(12.dp)
//                                            .background(Color(0xFFE53935), RoundedCornerShape(50))
//                                    )
//                                }
//                            }
//                        }
//                    )
                    Scaffold(
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = { scope.launch { sheetState.show() } },
                                backgroundColor = BluePrimary
                            ) {
                                Icon(Icons.Filled.FilterList, contentDescription = "Filter")
                                if (isFilterApplied.value) {
                                    Box(
                                        Modifier
                                            .padding(start = 20.dp, bottom = 20.dp)
                                            .size(12.dp)
                                            .background(Color(0xFFE53935), RoundedCornerShape(50))
                                    )
                                }
                            }
                        },
                        floatingActionButtonPosition = FabPosition.End
                    ) { contentPadding ->
                        // Screen content without TopAppBar
                        Column(
                            Modifier
                                .padding(contentPadding)
                                .padding(bottom = contentPadding.calculateBottomPadding() + 16.dp)
                        ) {
                            // Your content here
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
                                            .padding(vertical = 16.dp, horizontal = 24.dp)
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,


                                        ) {
                                        Text(
                                            text = "Transaction History",
                                            style = TextStyle(
                                                fontSize = 25.sp,
                                                textAlign = TextAlign.Center,
                                                color = Color.White
                                            ),
                                        )
                                        Text(
                                            text = "Income: Rp.${historyViewModel.recentIncome.value}",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 16.sp,
                                                textAlign = TextAlign.Left,
                                                color = Color.White
                                            ),
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                        Text(
                                            text = "Outcome: Rp.${historyViewModel.recentOutcome.value}",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 16.sp,
                                                textAlign = TextAlign.Left,
                                                color = Color.White
                                            ),
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        )
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
                                            TransactionItem(
                                                navController = navController,
                                                transaction = transaction
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


                }
            }
        )
    }
}

@Composable
fun TransactionItem(
    navController: NavController,
    transaction: Transaction,
    historyViewModel: HistoryViewModel = hiltViewModel()
) {
    val transaction = transaction
    var transactionAmount = "0"
    var transactionId = transaction.id ?: "0"
    if (transactionId == "") {
        transactionId = "Not available"
    }
    val merchantId = transaction.merchantId ?: "0"
    val transactionDate = transaction.timestamp?.let { historyViewModel.convertTimestampToDayMonthYear(it) } ?: "N/A"
    val transactionTime = transaction.timestamp?.let { historyViewModel.convertTimestampToHourMinute(it) } ?: "N/A"
    var transactionType = "N/A"

    var transactionTitle = "N/A"
    var transactionIcon = Icons.Default.QuestionMark
    var transactionSecondIcon = Icons.Default.QuestionMark
    var transactionSecondIconBackgroundColor = Color(0x00000000)
    var transactionPartyId = ""
    val amountFormatter = NumberFormat.getNumberInstance(Locale.US)

    if (transaction is PaymentTransaction) {
        transactionAmount = "+ IDR ${amountFormatter.format(transaction.amount ?: 0)}"
        transactionType = "Incoming"
        transactionTitle = "Customer Payment"
        transactionIcon = Icons.Default.Group
        transactionSecondIcon = Icons.Default.SouthWest
        transactionSecondIconBackgroundColor = Color(0xFF00C853)
        transactionPartyId = transaction.payerId ?: "0"
    } else if (transaction is MerchantWithdrawTransaction) {
        transactionAmount = "IDR ${amountFormatter.format(transaction.amount ?: 0)}"
        transactionType = "Outgoing"
        transactionTitle = "Withdraw to ${transaction.bankInst}"
        transactionIcon = Icons.Default.AccountBalance
        transactionSecondIcon = Icons.Default.NorthEast
        transactionSecondIconBackgroundColor = Color(0xFFE53935)
        transactionPartyId = transaction.bankAccountNo ?: "0"
    }

    Row(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable {
                navController.navigate(
                    "transactionItemDetails/${transactionTitle}/${transactionAmount}/${transactionId}/${merchantId}/${"$transactionDate • $transactionTime"}/${transactionType}/${transactionPartyId}"
                )
            },
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
                text = transactionTime,
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


//class ThousandSeparatorTransformation : VisualTransformation {
//    override fun filter(text: AnnotatedString): TransformedText {
//        val formatted = formatNumber(text.text)
//        val offsetTranslator = object : OffsetMapping {
//            override fun originalToTransformed(offset: Int): Int {
//                val commas = formatted.count { it == ',' }
//                return offset + commas
//            }
//            override fun transformedToOriginal(offset: Int): Int {
//                val commas = formatted.count { it == ',' }
//                return offset - commas
//            }
//        }
//        return TransformedText(AnnotatedString(formatted), offsetTranslator)
//    }
//
//    private fun formatNumber(input: String): String {
//        val number = input.toLongOrNull() ?: return input
//        val amountFormatter = java.text.NumberFormat.getNumberInstance(Locale.US)
//        return amountFormatter.format(number)
//    }
//}

