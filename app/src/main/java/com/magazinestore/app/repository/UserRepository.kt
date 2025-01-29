package com.magazinestore.app.repository

import android.content.Context
import android.util.Log
import com.magazinestore.app.MainActivity
import com.magazinestore.app.MyApplication
import com.magazinestore.app.network.LoginRequest
import com.magazinestore.app.network.LoginResponse
import com.magazinestore.app.network.NetworkApiService
import retrofit2.Response

class UserRepository(private val apiService: NetworkApiService) {
    suspend fun login(username: String, password: String): Response<LoginResponse> {
        val loginRequest = LoginRequest(username, password)
        return apiService.login(loginRequest)
    }
    private fun saveToken(token: String) {
        val sharedPreferences = MyApplication.instance.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)  // Save the token
        editor.apply()
    }
}

