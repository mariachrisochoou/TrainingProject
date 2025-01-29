package com.magazinestore.app.viewmodel

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.magazinestore.app.network.Magazine
import com.magazinestore.app.repository.MagazineRepository
import java.text.SimpleDateFormat
import java.util.Locale


class MagazineViewModel(private val magazineRepository: MagazineRepository) : ViewModel() {

//    private val _magazines = MutableLiveData<List<Magazine>>()
//    val magazines: LiveData<List<Magazine>> get() = _magazines


    suspend fun getMagazines(token: String): List<Magazine> {
        return try {
            magazineRepository.getMagazines(token)
        } catch (e: Exception) {
            Log.e("MagazineViewModel", "Error fetching magazines: ${e.message}")
            emptyList()
        }
    }

    fun groupMagazinesByMonthYear(magazines: List<Magazine>): Map<String, List<Magazine>> {
        val formatter = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)

        return magazines
            .filter {
                if (it.date_released == null) {
                    Log.e("MagazineViewModel", "ERROR: Magazine ${it.title} has a NULL date!")
                    false
                } else {
                    Log.d("MagazineViewModel", "Processing date: ${it.date_released}")
                    true
                }
            }
            .sortedByDescending { it.date_released }
            .groupBy {
                val monthYear = formatter.format(it.date_released!!)
                val englishMonth = SimpleDateFormat("MMM", Locale.ENGLISH).format(it.date_released)
                "$englishMonth ${it.date_released.year + 1900}"
            }
    }





    fun downloadMagazine(context: Context, magazine: Magazine) {
        val request = DownloadManager.Request(Uri.parse(magazine.pdf_url))
            .setTitle(magazine.title)
            .setDescription("Downloading magazine...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${magazine.title}.pdf")

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }


}


