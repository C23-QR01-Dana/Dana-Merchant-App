package com.dana.merchantapp.presentation.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dana.merchantapp.data.main.MainRepositoryImpl
import com.dana.merchantapp.domain.main.MainUseCase
import com.dana.merchantapp.presentation.landing.LandingActivity
import com.dana.merchantapp.presentation.ui.theme.MerchantAppTheme

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
fun Main(mainViewModel: MainViewModel) {
    Button(
        onClick = {
            mainViewModel.logoutUser()
        },
        shape = RoundedCornerShape(20.dp),

        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(20.dp))
    ) {
        Text(text = "Logout")
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