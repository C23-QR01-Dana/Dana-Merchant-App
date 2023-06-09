package com.dana.merchantapp.presentation.landing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dana.merchantapp.R
import com.dana.merchantapp.presentation.login.LoginActivity
import com.dana.merchantapp.presentation.register.RegisterActivity
import com.dana.merchantapp.presentation.ui.theme.BluePrimary
import com.dana.merchantapp.presentation.ui.theme.MerchantAppTheme

class LandingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MerchantAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LandingScreen(this)
                }
            }
        }
    }
}


@Composable
fun LandingScreen(context: Context) {

    val scrollState = rememberScrollState()

    Card (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .shadow(8.dp, shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo_dana),
                contentDescription = "Logo Dana",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .padding(20.dp)
            )
            Button(
                onClick = {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                },
                shape = RoundedCornerShape(20.dp),

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), spotColor = Color.LightGray)
            ) {
                Text(text = "Login")
            }

            Button(
                onClick = {
                    val intent = Intent(context, RegisterActivity::class.java)
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), spotColor = Color.LightGray)

            ) {
                Text(text = "Register", color = BluePrimary)
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val context = LocalContext.current
    MerchantAppTheme {
        LandingScreen(context)
    }
}
