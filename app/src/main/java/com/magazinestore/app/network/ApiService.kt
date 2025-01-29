package com.magazinestore.app.network


import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.Date


interface NetworkApiService {
    @POST("Access/Login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("Access/Books")
    suspend fun getMagazines(@Header("Authorization") authorization: String): Response<List<Magazine>>
}

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(
    val expires_in: Int,
    val token_type: String,
    val refresh_token: String,
    val access_token: String
)

data class Magazine(
    val id: Int,
    val title: String,
    @SerializedName("img_url") val image_url: String,
    val date_released: Date?,
    @SerializedName("pdf_url") val pdf_url: String
)
