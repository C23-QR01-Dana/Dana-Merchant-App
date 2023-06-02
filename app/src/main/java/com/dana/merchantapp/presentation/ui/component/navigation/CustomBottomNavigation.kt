package com.dana.merchantapp.presentation.ui.component.navigation

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun CustomBottomNavigation(
    navController: NavHostController
) {
    BottomAppBar(
        cutoutShape = CircleShape,
        backgroundColor = Color.White ,
        modifier = Modifier.fillMaxWidth()
    ) {
        BottomNavigation(
            backgroundColor = Color.White,
            contentColor = MaterialTheme.colors.primary
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val items = listOf(
                BottomNavItem("Home", Icons.Default.Home, Screen.Home),
                BottomNavItem("History", Icons.Default.History, Screen.History),
                BottomNavItem("Bank", Icons.Default.CreditCard, Screen.Withdrawal),
                BottomNavItem("Profile", Icons.Default.Person, Screen.Profile)
            )

            items.forEachIndexed { index, item ->
                BottomNavigationItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(item.title) },
                    selected = currentRoute == item.screen.route,
                    unselectedContentColor = Color.Black,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
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