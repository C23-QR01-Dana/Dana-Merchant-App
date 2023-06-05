package com.dana.merchantapp.presentation.screen.qr.scanqr



import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.dana.merchantapp.presentation.main.MainActivity
import com.dana.merchantapp.presentation.ui.component.navigation.Screen
import com.dana.merchantapp.presentation.ui.theme.BluePrimary
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


@Composable
fun ScanResultScreen(scanResultViewModel: ScanResultViewModel = hiltViewModel(), navController: NavController, harga: Int, userId: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    scanResultViewModel.transactionResult.observe(lifecycleOwner) { isSuccess ->
        if (isSuccess) {
            Toast.makeText(context, "Payment Success", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.History.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
            }
        }
        else{
            Toast.makeText(context, "Payment Error", Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Transaksi",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = userId,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.ShoppingCartCheckout,
                contentDescription = "Icon Uang",
                tint = BluePrimary,
                modifier = Modifier.size(200.dp)
            )
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Harga",
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Rp.${harga}",
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.navigateUp() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Batal")
                }
                Button(
                    onClick = {
                        scanResultViewModel.transaction(amount=harga, payerId=userId, timestamp= System.currentTimeMillis(), trxType="PAYMENT")

                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Bayar")
                }
            }
        }



    }

//    Column() {
//
//        Button(
//            onClick = {
//                navController.navigate(Screen.History.route){
//                    popUpTo(navController.graph.findStartDestination().id) {
//                        saveState = true
//                    }
//                    launchSingleTop = true
//                }
//            },
//            shape = RoundedCornerShape(20.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp)
//
//        ) {
//            Text(text = "Generate QR")
//        }
//    }


}