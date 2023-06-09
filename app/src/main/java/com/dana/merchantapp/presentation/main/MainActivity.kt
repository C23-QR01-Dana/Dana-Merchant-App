package com.dana.merchantapp.presentation.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dana.merchantapp.presentation.screen.*
import com.dana.merchantapp.presentation.landing.LandingActivity
import com.dana.merchantapp.presentation.screen.history.HistoryScreen
import com.dana.merchantapp.presentation.screen.home.HomeScreen
import com.dana.merchantapp.presentation.screen.profile.ProfileScreen
import com.dana.merchantapp.presentation.screen.qr.QrScreen
import com.dana.merchantapp.presentation.screen.qr.scanqr.ScanCameraScreen
import com.dana.merchantapp.presentation.screen.qr.scanqr.ScanResultScreen
import com.dana.merchantapp.presentation.screen.withdrawal.AddBankScreen
import com.dana.merchantapp.presentation.screen.withdrawal.BankSelectScreen
import com.dana.merchantapp.presentation.screen.withdrawal.WithdrawAmountScreen
import com.dana.merchantapp.presentation.screen.withdrawal.WithdrawalScreen
import com.dana.merchantapp.presentation.ui.component.navigation.BottomNavItem
import com.dana.merchantapp.presentation.ui.component.navigation.CustomBottomNavigation
import com.dana.merchantapp.presentation.ui.component.navigation.Screen
import com.dana.merchantapp.presentation.ui.theme.BlueButton
import com.dana.merchantapp.presentation.ui.theme.MerchantAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        setContent {
            MerchantAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Main()
                }
            }
        }
    }

    private fun createNotificationChannel() {
        // If the Android Version is greater than Oreo,
        // then create the NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "PAYMENT"
            val descriptionText = "Notification for incoming payment"

            val channel = NotificationChannel(
                "PAYMENT_1",
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionText
            }

            // Register the channel
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@Composable
fun Main(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute == Screen.Home.route || currentRoute == Screen.History.route || currentRoute == Screen.Withdrawal.route || currentRoute == Screen.Profile.route ) {
                CustomBottomNavigation(navController)
            }
        },
        floatingActionButton = {
            if (currentRoute == Screen.Home.route || currentRoute == Screen.History.route || currentRoute == Screen.Withdrawal.route || currentRoute == Screen.Profile.route ) {
                FloatingActionButton(
                    onClick = {
                        val item = BottomNavItem("QR", Icons.Default.QrCode, Screen.QR)
                        navController.navigate(Screen.QR.createRoute(0))
                    },
                    backgroundColor = BlueButton

                ) {
                    Icon(Icons.Default.QrCode, contentDescription = "QR", tint = Color.White)
                }
            }

        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }

            composable(
                route = Screen.QR.route,
                arguments = listOf(
                    navArgument("tabIndex") { type = NavType.IntType }
                )
            ) {
                val id = it.arguments?.getInt("tabIndex") ?: 0
                QrScreen(
                    navController = navController,
                    initialTabIndex = id,
                )
            }
            composable(
                route = Screen.ScanCamera.route,
                arguments = listOf(
                    navArgument("harga") { type = NavType.IntType }
                )
            ) {
                val id = it.arguments?.getInt("harga") ?: -1
                ScanCameraScreen(
                    navController = navController,
                    harga = id,
                )
            }
            composable(
                route = Screen.ScanResult.route,
                arguments = listOf(
                    navArgument("harga") { type = NavType.IntType },
                    navArgument("userId") { type = NavType.StringType }
                )
            ) {
                val price = it.arguments?.getInt("harga") ?: -1
                val id = it.arguments?.getString("userId") ?: ""
                ScanResultScreen(
                    navController=navController,
                    harga = price,
                    userId = id
                )
            }
            composable(Screen.Withdrawal.route) {
                WithdrawalScreen(navController)
            }
            composable(Screen.History.route) {
                HistoryScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }
            composable(Screen.BankSelect.route) {
                BankSelectScreen(navController)
            }
            composable(Screen.AddBank.route) {
                AddBankScreen(navController)
            }
            composable(
                route = Screen.WithdrawAmount.route,
                arguments = listOf(
                    navArgument("bankAccountNo") { type = NavType.StringType },
                    navArgument("bankInst") { type = NavType.StringType },
                )
            ) {
                val bankAccountNo = it.arguments?.getString("bankAccountNo") ?: ""
                val bankInst = it.arguments?.getString("bankInst") ?: ""
                WithdrawAmountScreen(navController, bankAccountNo, bankInst)
            }
        }
        // Content area
    }

}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    val mainRepository = MainRepositoryImpl()
//    val mainUseCase = MainUseCase(mainRepository)
//    val mainViewModel = MainViewModel(mainUseCase)
//
//    MerchantAppTheme {
//        Main(mainViewModel)
//    }
//}