package com.magazinestore.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.magazinestore.app.repository.UserRepository
import com.magazinestore.app.viewmodel.LoginViewModel

class LoginViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
