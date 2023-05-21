package com.dana.merchantapp.presentation.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dana.merchantapp.presentation.model.BottomNavItem

@Composable
fun CustomBottomNavigation(
) {
    BottomAppBar(
        cutoutShape = CircleShape,
        backgroundColor = Color.White ,
        modifier = Modifier.fillMaxWidth()
    ) {
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.background,
            contentColor = MaterialTheme.colors.primary
        ) {

            val items = listOf(
                BottomNavItem("Home", Icons.Default.Home, "home"),
                BottomNavItem("History", Icons.Default.History, "history"),
                BottomNavItem("Bank", Icons.Default.CreditCard, "withdrawal"),
                BottomNavItem("Profile", Icons.Default.Person, "profile")
            )

            items.forEachIndexed { index, item ->
                BottomNavigationItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(item.title) },
                    selected = "" == item.route,
                    unselectedContentColor = Color.Black,
                    onClick = {}
                )


                if (index == 1) {
                    Spacer(
                        modifier = Modifier.weight(1f, fill = true)
                    )
                }
            }


        }
    }
}