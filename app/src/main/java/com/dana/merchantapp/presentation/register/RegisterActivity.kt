package com.dana.merchantapp.presentation.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dana.merchantapp.R

import com.dana.merchantapp.presentation.main.MainActivity
import com.dana.merchantapp.presentation.ui.component.CustomTextField
import com.dana.merchantapp.presentation.ui.theme.BluePrimary
import com.dana.merchantapp.presentation.ui.theme.MerchantAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MerchantAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RegisterScreen()
                }
            }
        }
    }
}


@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    registerViewModel.registerResult.observe(lifecycleOwner) { isSuccess ->
        if (isSuccess) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    registerViewModel.registerMessage.observe(lifecycleOwner) { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordHidden by remember { mutableStateOf(true) }
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
                    .size(172.dp)
                    .padding(12.dp)
            )

            CustomTextField(
                value = name,
                onValueChange = { name = it },
                label = "Name",
                keyboardType = KeyboardType.Text
            )
            CustomTextField(
                value = address,
                onValueChange = { address = it },
                label = "Address",
                keyboardType = KeyboardType.Text
            )
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                keyboardType = KeyboardType.Email
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password", color = BluePrimary) },
                visualTransformation =  if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.primary),
                trailingIcon = {
                    IconButton(onClick = { passwordHidden = !passwordHidden }) {
                        val visibilityIcon =
                            if (passwordHidden) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        // Please provide localized description for accessibility services
                        val description = if (passwordHidden) "Show password" else "Hide password"
                        Icon(imageVector = visibilityIcon, contentDescription = description, tint = BluePrimary)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Button(
                onClick = {
                registerViewModel.registerUser(name,address, email, password)
                },
                shape = RoundedCornerShape(20.dp),

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(20.dp))
            ) {
                Text(text = "Register")
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {



    MerchantAppTheme {
        RegisterScreen()
    }
}
