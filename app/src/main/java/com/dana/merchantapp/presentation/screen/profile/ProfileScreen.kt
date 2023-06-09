package com.dana.merchantapp.presentation.screen.profile

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.rememberImagePainter
import com.dana.merchantapp.presentation.landing.LandingActivity
import com.dana.merchantapp.presentation.ui.theme.BluePrimary
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController, profileViewModel: ProfileViewModel = hiltViewModel()) {
    val maxFileSizeBytes = 5 * 1024 * 1024
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            val contentResolver = context.contentResolver
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val fileSizeBytes = inputStream.available()
                if (fileSizeBytes > maxFileSizeBytes) {
                    coroutineScope.launch {
                        Toast.makeText(context, "File Size Must Not Exceed 5 MB", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    profileViewModel.updatePhoto(uri)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.getMerchant()
    }

    val isSuccess: Boolean by profileViewModel.logoutResult

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

    val isUploading: Boolean by profileViewModel.isUploading

    LaunchedEffect(isUploading) {
        if (isUploading) {
            coroutineScope.launch {
                Toast.makeText(context, "Uploading...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
//        TopAppBar(
//            title = { Text("Profile") },
//
//            modifier = Modifier
//                .background(
//                    brush = Brush.linearGradient(
//                        colors = listOf(Color(0xFF86B0FF), Color(0xFF408CE2)),
//                        start = Offset.Zero,
//                        end = Offset.Infinite
//                    )
//                )
//        )
        Card(
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(0.dp),
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
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center

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
                                    color = Color.White
                                ),
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                            )
                            Text(
                                text = profileViewModel.merchant.value?.email ?: "",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = Color.White
                                ),
                                modifier = Modifier
                            )
                            Text(
                                text = profileViewModel.merchant.value?.merchantAddress ?: "",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = Color.White
                                ),
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
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
                    text = ("Rp." + profileViewModel.merchant.value?.balance.toString()) ?: "0",
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
                    shape = RoundedCornerShape(8.dp),
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
            shape = RoundedCornerShape(8.dp),

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