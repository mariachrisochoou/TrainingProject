package com.magazinestore.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.magazinestore.app.navigation.AppNavigation
import com.magazinestore.app.network.ApiClient
import com.magazinestore.app.network.NetworkApiService
import com.magazinestore.app.repository.UserRepository
import com.magazinestore.app.ui.theme.AppTheme
import com.magazinestore.app.viewmodel.LoginViewModel
import com.magazinestore.app.viewmodel.LoginViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val apiService = ApiClient.retrofit.create(NetworkApiService::class.java)
        val repository = UserRepository(apiService)
        val viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        setContent {
            val loginViewModel: LoginViewModel = viewModel()

            AppTheme {
                val navController = rememberNavController()

                AppNavigation(
                    navController = navController,
                    loginViewModel = loginViewModel
                )

            }
        }
    }
}


