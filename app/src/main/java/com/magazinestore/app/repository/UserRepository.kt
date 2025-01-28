package com.magazinestore.app.repository

import com.magazinestore.app.network.LoginRequest
import com.magazinestore.app.network.LoginResponse
import com.magazinestore.app.network.NetworkApiService
import retrofit2.Response

class UserRepository(private val apiService: NetworkApiService) {
    suspend fun login(username: String, password: String): Response<LoginResponse> {
        val loginRequest = LoginRequest(username, password)

        return apiService.login(loginRequest)
    }

//    suspend fun getMagazines(): List<Magazine> {
//        return apiService.getMagazines()
//    }
}
