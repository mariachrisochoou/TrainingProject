package com.magazinestore.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.magazinestore.app.ui.screens.LoginPage
import com.magazinestore.app.ui.screens.MagazinePage
import com.magazinestore.app.viewmodel.LoginViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginPage(
                loginViewModel = loginViewModel,
                onLoginSuccess = { token ->
                    navController.navigate("magazine") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("magazine") {
            MagazinePage()
        }
    }
}

