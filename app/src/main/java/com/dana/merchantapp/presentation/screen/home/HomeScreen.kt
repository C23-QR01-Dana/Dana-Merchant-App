package com.dana.merchantapp.presentation.screen.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.dana.merchantapp.presentation.main.MainViewModel

@Composable
fun HomeScreen(mainViewModel: MainViewModel) {
    Text("Home")
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