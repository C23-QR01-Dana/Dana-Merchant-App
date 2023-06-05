package com.dana.merchantapp.presentation.main

import android.content.Intent
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
import com.dana.merchantapp.data.main.MainRepositoryImpl
import com.dana.merchantapp.domain.main.MainUseCase
import com.dana.merchantapp.presentation.screen.*
import com.dana.merchantapp.presentation.landing.LandingActivity
import com.dana.merchantapp.presentation.screen.history.HistoryScreen
import com.dana.merchantapp.presentation.screen.home.HomeScreen
import com.dana.merchantapp.presentation.screen.profile.ProfileScreen
import com.dana.merchantapp.presentation.screen.qr.QrScreen
import com.dana.merchantapp.presentation.screen.qr.scanqr.ScanCameraScreen
import com.dana.merchantapp.presentation.screen.qr.scanqr.ScanResultScreen
import com.dana.merchantapp.presentation.screen.withdrawal.WithdrawalScreen
import com.dana.merchantapp.presentation.ui.component.navigation.BottomNavItem
import com.dana.merchantapp.presentation.ui.component.navigation.CustomBottomNavigation
import com.dana.merchantapp.presentation.ui.component.navigation.Screen
import com.dana.merchantapp.presentation.ui.theme.BlueButton
import com.dana.merchantapp.presentation.ui.theme.MerchantAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainRepository = MainRepositoryImpl()
        val mainUseCase = MainUseCase(mainRepository)
        mainViewModel = MainViewModel(mainUseCase)

        mainViewModel.logoutResult.observe(this) { isSuccess ->
            if (isSuccess) {
                val intent = Intent(this, LandingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        mainViewModel.logoutMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            MerchantAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Main(mainViewModel)
                }
            }
        }
    }
}

@Composable
fun Main(mainViewModel: MainViewModel,navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.QR.route && currentRoute != Screen.ScanCamera.route && currentRoute != Screen.ScanResult.route) {
                CustomBottomNavigation(navController)
            }
        },
        floatingActionButton = {
            if (currentRoute != Screen.QR.route && currentRoute != Screen.ScanCamera.route && currentRoute != Screen.ScanResult.route) {
                FloatingActionButton(
                    onClick = {
                        val item = BottomNavItem("QR", Icons.Default.QrCode, Screen.QR)
                        navController.navigate(item.screen.route)
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
                HomeScreen(mainViewModel)
            }
            composable(Screen.QR.route) {
                QrScreen(navController)
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
                WithdrawalScreen()
            }
            composable(Screen.History.route) {
                HistoryScreen()
            }

            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }
        // Content area
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val mainRepository = MainRepositoryImpl()
    val mainUseCase = MainUseCase(mainRepository)
    val mainViewModel = MainViewModel(mainUseCase)

    MerchantAppTheme {
        Main(mainViewModel)
    }
}