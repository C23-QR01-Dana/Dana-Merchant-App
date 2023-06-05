package com.dana.merchantapp.presentation.screen.qr.scanqr



import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.dana.merchantapp.presentation.ui.component.navigation.Screen


@Composable
fun ScanResultScreen(navController: NavController, harga: Int, userId: String) {
    Column() {
        Text("${harga}")
        Text(userId)
        Button(
            onClick = {
                navController.navigate(Screen.History.route){
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                }
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)

        ) {
            Text(text = "Generate QR")
        }
    }


}