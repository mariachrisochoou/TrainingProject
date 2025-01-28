//package com.magazinestore.app.navigation
//
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//import com.magazinestore.app.Routes
//import java.lang.reflect.Modifier
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.compose.material3.BottomNavigation
//import androidx.compose.material3.BottomNavigationItem
//import androidx.compose.ui.graphics.Color
//
//@Composable
//fun BottomNavigationBar(navController: NavController) {
//    BottomNavigation(
//        backgroundColor = MaterialTheme.colorScheme.onSurface,
//        contentColor = MaterialTheme.colorScheme.primary,
//        modifier = Modifier
//            .padding(16.dp)
//            .clip(RoundedCornerShape(16.dp))
//    ) {
//
//        BottomNavigationItem(
//            icon = { Icon(Icons.Default.Home, contentDescription = "Magazine", tint = Color(0xFF28961B)) },
//            selected = false,
//            onClick = { navController.navigate(Routes.magazine) }
//        )
//
//    }
//}
//
//@Composable
//fun NavigationGraph(navController: NavHostController, modifier: Modifier) {
//    NavHost(
//        navController = navController,
//        startDestination = Routes.login,
//        modifier = modifier
//    ) {
//        composable(Routes.login) {
//            TODO()
//        }
//
//    }
//}