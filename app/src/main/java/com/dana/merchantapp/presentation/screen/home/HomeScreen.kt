package com.dana.merchantapp.presentation.screen.home


import android.icu.text.NumberFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.QrCodeScanner
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.dana.merchantapp.data.model.MerchantWithdrawTransaction
import com.dana.merchantapp.data.model.PaymentTransaction
import com.dana.merchantapp.data.model.Transaction
import com.dana.merchantapp.presentation.screen.history.HistoryViewModel
import com.dana.merchantapp.presentation.ui.component.navigation.Screen
import com.dana.merchantapp.presentation.ui.theme.BluePrimary
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {
    val transactions = homeViewModel.transactions.value
    val sortedTransactions = transactions?.sortedByDescending { it.timestamp }?.take(3)

    LaunchedEffect(Unit) {
        homeViewModel.getMerchant()
        homeViewModel.getTransactions()
    }

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() = refreshScope.launch {
        refreshing = true
        homeViewModel.getMerchant()
        homeViewModel.getTransactions()
        refreshing = false
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    Box(Modifier.pullRefresh(state)) {
        LazyColumn(
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
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
                                text = "Hello, ${homeViewModel.merchant.value?.merchantName ?: ""}",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp,
                                    textAlign = TextAlign.Left,
                                    color = Color.White
                                ),
                            )
                            Text(
                                text = "Balance:",
                                style = TextStyle(
                                    fontWeight = FontWeight.Light,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Left,
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(top = 16.dp)
                            )
                            Text(
                                text = "Rp.${homeViewModel.merchant.value?.balance ?: ""}",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Left,
                                    color = Color.White
                                ),

                                )
                        }
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ShortcutCard(
                        title = "Static",
                        icon = Icons.Default.QrCode,
                        onClick = {
                            navController.navigate(Screen.QR.createRoute(0))
                        }
                    )
                    ShortcutCard(
                        title = "Scan",
                        icon = Icons.Default.QrCodeScanner,
                        onClick = {
                            navController.navigate(Screen.QR.createRoute(1))
                        }
                    )
                    ShortcutCard(
                        title = "Dynamic",
                        icon = Icons.Default.QrCode2,
                        onClick = {
                            navController.navigate(Screen.QR.createRoute(2))
                        }
                    )
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    if (sortedTransactions != null) {
                        BriefHistory(sortedTransactions, navController)
                    }
                }
            }
        }
//        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }

}

@Composable
fun ShortcutCard(title: String,icon:ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .size(95.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Shortcut Icon",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = title,
                    style = TextStyle(color = BluePrimary, fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun BriefHistory(transactions: List<Transaction>, navController: NavController) {
    Column () {
        Text(
            text = "Recent Transaction",
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            style = MaterialTheme.typography.h6
        )
        transactions.forEach {
            Item(transaction = it)
        }
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.LightGray
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "See More",
                style = TextStyle(
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { navController.navigate("history") {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                               },
            )
        }
    }
}

@Composable
fun Item(transaction: Transaction, historyViewModel: HomeViewModel = hiltViewModel()) {
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




