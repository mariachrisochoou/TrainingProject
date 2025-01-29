package com.magazinestore.app.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.magazinestore.app.R
import com.magazinestore.app.network.ApiClient
import com.magazinestore.app.network.Magazine
import com.magazinestore.app.network.NetworkApiService
import com.magazinestore.app.repository.MagazineRepository
import com.magazinestore.app.viewmodel.MagazineViewModel
import com.magazinestore.app.viewmodel.MagazineViewModelFactory
import java.io.File


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun MagazinePage(token: String?) {

    val apiService = ApiClient.retrofit.create(NetworkApiService::class.java)
    val repository = MagazineRepository(apiService)

    val viewModelFactory = MagazineViewModelFactory(repository)
    val magazineViewModel: MagazineViewModel = viewModel(factory = viewModelFactory)
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    var magazines by remember { mutableStateOf<List<Magazine>>(emptyList()) }

    LaunchedEffect(Unit) {
        if (token != null) {
            try {
                isLoading = true
                magazines = magazineViewModel.getMagazines(token)
                isLoading = false
            } catch (e: Exception) {
                isLoading = false
                errorMessage = "Failed to load magazines"
                Log.e("MagazinePage", "Error loading magazines: ${e.message}", e)

            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = Color.White
                ),
                title = {
                    Text(
                        text = stringResource(R.string.mag_title),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorMessage != null) {
                Text(text = errorMessage ?: "Something went wrong")
            } else {
                Log.d("ouf", "$magazines")
                val groupedMagazines = magazineViewModel.groupMagazinesByMonthYear(magazines)
                Log.d("ouf", "2: $groupedMagazines")

                LazyColumn {

                    groupedMagazines.forEach { (monthYear, magazinesInSection) ->
                        stickyHeader {
                            Text(
                                text = monthYear,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                        items(magazinesInSection) { magazine ->
                            MagazineItem(magazine, magazineViewModel)
                        }
                    }
                }
                Scaffold(
                    bottomBar = { BottomNavBar() }
                ) {
                }
            }




        }

        }
    }



fun openPdf(context: Context, pdfUrl: String) {
    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pdfUrl)
    val uri = FileProvider.getUriForFile(context, "com.magazinestore.app.fileprovider", file)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(intent)
}


@Composable
fun MagazineItem(magazine: Magazine, magazineViewModel: MagazineViewModel) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                openPdf(context, magazine.pdf_url)
            }
    ) {
        Image(
            painter = rememberImagePainter(magazine.image_url),
            contentDescription = magazine.title,
            modifier = Modifier.size(100.dp).padding(end = 16.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = magazine.title, style = MaterialTheme.typography.bodyLarge)
            Text(text = magazine.date_released.toString(), style = MaterialTheme.typography.bodySmall)
        }

        IconButton(onClick = {
            magazineViewModel.downloadMagazine(context, magazine)
        }) {
            Icon(imageVector = Icons.Default.Download, contentDescription = "Download")
        }
    }
}
