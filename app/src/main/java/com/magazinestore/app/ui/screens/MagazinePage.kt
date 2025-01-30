package com.magazinestore.app.ui.screens

import android.annotation.SuppressLint
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.magazinestore.app.R
import com.magazinestore.app.network.ApiClient
import com.magazinestore.app.network.Magazine
import com.magazinestore.app.network.NetworkApiService
import com.magazinestore.app.repository.MagazineRepository
import com.magazinestore.app.viewmodel.MagazineViewModel
import com.magazinestore.app.viewmodel.MagazineViewModelFactory
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
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
        },
        bottomBar = { BottomNavBar() }
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
                val groupedMagazines = magazineViewModel.groupMagazinesByMonthYear(magazines)

                Spacer(modifier = Modifier.size(80.dp))
                LazyColumn {

                    groupedMagazines.forEach { (monthYear, magazinesInSection) ->
                        Log.d("Grouped", monthYear)
                        item {
                            Text(
                                text = monthYear,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }

                        item {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(magazinesInSection) { magazine ->
                                    MagazineItem(magazine, magazineViewModel)
                                }
                            }
                        }
                    }
                }
            }

        }

        }
    }





@Composable
fun MagazineItem(magazine: Magazine, magazineViewModel: MagazineViewModel) {
    val context = LocalContext.current
    var isDownloaded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "${magazine.title}.pdf"
                )
                if (file.exists()) {
                    magazineViewModel.openPdf(context, file.toString())
                } else {
                    Toast.makeText(context, "File not downloaded", Toast.LENGTH_SHORT).show()
                }
            }
    ) {

        Column(
            modifier = Modifier
                .size(width = 110.dp, height = 190.dp)
                .align(Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(magazine.image_url),
                    contentDescription = magazine.title,
                    modifier = Modifier
                        .size(width = 110.dp, height = 130.dp)
                        .background(MaterialTheme.colorScheme.secondary)
                        .clickable {
                            magazineViewModel.downloadMagazine(context, magazine)

                        },
                    contentScale = ContentScale.Crop
                )

                if (isDownloaded) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Downloaded",
                        tint = Color.Green,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)
                            .size(24.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(4.dp))
                    )
                }

                if (!isDownloaded) {
                    IconButton(
                        onClick = {
                            magazineViewModel
                                .downloadMagazine(context, magazine)
                            isDownloaded = true
                        },
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download"
                        )
                    }
                }
            }


            Text(
                text = magazine.title,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .wrapContentWidth()
            )

        }


    }
}
