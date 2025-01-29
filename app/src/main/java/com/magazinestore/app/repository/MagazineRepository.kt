package com.magazinestore.app.repository

import android.util.Log
import com.magazinestore.app.network.Magazine
import com.magazinestore.app.network.NetworkApiService
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MagazineRepository(private val apiService: NetworkApiService) {

    suspend fun getMagazines(token: String): List<Magazine> {
        try {

            val response = apiService.getMagazines("Bearer $token")

            if (response.isSuccessful) {
                val magazines = response.body() ?: emptyList()
                return magazines.map { magazine ->
                    Magazine(
                        id = magazine.id,
                        title = magazine.title,
                        image_url = magazine.image_url,
                        date_released = parseDate(magazine.date_released.toString()),
                        pdf_url = magazine.pdf_url
                    )
                }
            } else {
                Log.e("APIError", "Error fetching magazines 1: ${response.message()}")
                return emptyList()
            }
        } catch (e: Exception) {
            Log.e("APIError", "Error fetching magazines: ${e.message}")
            return emptyList()
        }
    }



    fun parseDate(dateString: String?): Date? {
        if (dateString.isNullOrEmpty()) return null

        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

        return try {
            dateFormat.parse(dateString)
        } catch (e: ParseException) {
            Log.e("MagazineViewModel", "ERROR parsing date: $dateString", e)
            null
        }
    }
}


