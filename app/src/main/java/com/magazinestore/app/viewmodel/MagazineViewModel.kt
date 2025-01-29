package com.magazinestore.app.viewmodel

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magazinestore.app.network.Magazine
import com.magazinestore.app.repository.MagazineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale


class MagazineViewModel(private val magazineRepository: MagazineRepository) : ViewModel() {

    private val _magazines = MutableLiveData<List<Magazine>>()
    val magazines: LiveData<List<Magazine>> get() = _magazines

//    fun getMagazines(token: String) {
//        viewModelScope.launch {
//            val magazinesList = magazineRepository.getMagazines(token)
//
//            _magazines.value = magazinesList
//            Log.d("MagazineViewModel", "Updated magazines: ${magazines.value}")
//
//        }
//    }
    suspend fun getMagazines(token: String): List<Magazine> {
        return try {
            magazineRepository.getMagazines(token)
        } catch (e: Exception) {
            Log.e("MagazineViewModel", "Error fetching magazines: ${e.message}")
            emptyList()
        }
    }


    fun groupMagazinesByMonthYear(magazines: List<Magazine>): Map<String, List<Magazine>> {
        val formatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

        // Filter and log invalid magazines
        val validMagazines = magazines.map {
            if (it.date_released == null) {
                Log.e("MagazineViewModel", "Magazine with null date: ${it.title}")
                // Handle null dates by using a default date
                it.copy(date_released = SimpleDateFormat("yyyy-MM-dd").parse("1900-01-01"))
            } else {
                it
            }
        }

        // Group by formatted date
        return validMagazines.groupBy { formatter.format(it.date_released) }
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


