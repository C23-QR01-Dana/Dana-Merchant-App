package com.dana.merchantapp.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dana.merchantapp.R
import com.dana.merchantapp.presentation.landing.LandingActivity
import com.dana.merchantapp.presentation.main.MainActivity
import com.dana.merchantapp.presentation.ui.theme.MerchantAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MerchantAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SplashScreen(
                        navigateToLogin = { navigateToLoginActivity() },
                        navigateToMain = { navigateToMainActivity() }
                    )
                }
            }
        }

    }
    private fun navigateToLoginActivity() {
        val intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
    navigateToMain: () -> Unit,

) {
    val isLogin by splashViewModel.isLogin.observeAsState(false)

    LaunchedEffect(key1 = isLogin) {
        delay(1500)
        if (isLogin) {
            navigateToMain()
        } else {
            navigateToLogin()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(color= Color.White),
        contentAlignment = Alignment.Center
    ) {
        // Add your splash screen content here, such as logo or animation
        Image(
            painter = painterResource(R.drawable.logo_dana),
            contentDescription = "Logo Dana",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .padding(20.dp)
        )
    }
}

