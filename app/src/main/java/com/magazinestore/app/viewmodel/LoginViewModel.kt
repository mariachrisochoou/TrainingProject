package com.magazinestore.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magazinestore.app.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _token = MutableLiveData<String?>()
    val token: MutableLiveData<String?> = _token

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> = _errorMessage

    private val _usernameError = MutableLiveData<String?>(null)
    val usernameError: LiveData<String?> = _usernameError

    private val _passwordError = MutableLiveData<String?>(null)
    val passwordError: LiveData<String?> = _passwordError

    fun login(username: String, password: String) {
        _usernameError.value = null
        _passwordError.value = null

        if (username.isEmpty() || password.isEmpty()) {
            _usernameError.value = "Username is required"
            _passwordError.value = "Password is required"
            errorMessage.value = "Please fill in all fields"
            return
        }

        viewModelScope.launch {
            try {
                val response = repository.login(username, password)

                if (response.isSuccessful) {
                    val accessToken = response.body()?.access_token
                    if (accessToken != null) {
                        _token.value = accessToken
                    } else {
                        _errorMessage.value = "Token missing in response"
                    }

                } else {
                    _errorMessage.postValue("Login failed")
                    _usernameError.value = "Something is wrong"
                    _passwordError.value = "Something is wrong"
                }
            } catch (e: Exception) {
                _errorMessage.postValue("An error occurred: ${e.message}")
            }
        }
    }
}


