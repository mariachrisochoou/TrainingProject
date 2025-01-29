package com.magazinestore.app.repository

import android.util.Log
import com.magazinestore.app.network.Magazine
import com.magazinestore.app.network.NetworkApiService
import java.text.SimpleDateFormat
import java.util.*
import retrofit2.Response

class MagazineRepository(private val apiService: NetworkApiService) {

    suspend fun getMagazines(token: String): List<Magazine> {
        try {

            val response = apiService.getMagazines("Bearer $token")

            if (response.isSuccessful) {
                val magazines = response.body() ?: emptyList()
//                return magazines
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

        val dateFormats = listOf(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()),
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
            SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        )

        for (format in dateFormats) {
            try {
                return format.parse(dateString)
            } catch (e: Exception) {

                e.printStackTrace()
            }
        }

        return null
    }


}


