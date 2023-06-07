package com.dana.merchantapp.presentation.screen.profile

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Payment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.ImagePainter.State.Empty.painter
import coil.compose.rememberImagePainter
import com.dana.merchantapp.R
import com.dana.merchantapp.presentation.landing.LandingActivity
import com.dana.merchantapp.presentation.ui.theme.BluePrimary

@Composable
fun ProfileScreen(navController: NavController, profileViewModel: ProfileViewModel = hiltViewModel()) {
    val maxFileSizeBytes = 5 * 1024 * 1024
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            val contentResolver = context.contentResolver
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val fileSizeBytes = inputStream.available()
                if (fileSizeBytes > maxFileSizeBytes) {
                    Log.w("kegedean", "File size is too large")
                } else {
                    profileViewModel.updatePhoto(uri)
                }
            }
        }
    }

    val isSuccess: Boolean by profileViewModel.logoutResult.observeAsState(false)

    LaunchedEffect(Unit) {
        profileViewModel.getMerchant()
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            // Navigate to the LandingActivity
            val intent = Intent(
                context,
                LandingActivity::class.java
            ).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
        ) {
            Box(modifier = Modifier.size(110.dp)) {
                Image(
                    painter = rememberImagePainter(
                        data = profileViewModel.merchant.value?.merchantLogo ?: ""
                    ),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(BluePrimary)
                        .clickable {
                            launcher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Change Photo",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(16.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = profileViewModel.merchant.value?.merchantName ?: "",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = profileViewModel.merchant.value?.email ?: "",
                    style = TextStyle(
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                )
                Text(
                    text = profileViewModel.merchant.value?.merchantAddress ?: "",
                    style = TextStyle(
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = "Balance",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = ("RP." + profileViewModel.merchant.value?.balance.toString()) ?: "0",
                    style = TextStyle(
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier

            ) {
                Button(
                    onClick = {
                          navController.navigate("withdrawal") {
                              popUpTo(navController.graph.findStartDestination().id) {
                                  saveState = true
                              }
                              restoreState = true
                              launchSingleTop = true
                          }
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .height(55.dp)
                        .width(55.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Payment",
                        modifier = Modifier.size(48.dp)
                    )
                }
                Text(
                    text = "Withdraw",
                    style = MaterialTheme.typography.body2,
                )
            }
        }

        Button(
            onClick = {
                profileViewModel.logoutUser()
            },
            shape = RoundedCornerShape(20.dp),

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Logout")
        }
    }
}

//@Preview
//@Composable
//fun ProfileScreenPreview() {
//    ProfileScreen()
//}