package com.magazinestore.app.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Magazine : BottomNavItem(Routes.magazine, Icons.Filled.MenuBook , "Magazine")
    data object Scan : BottomNavItem(Routes.scan, Icons.Filled.QrCodeScanner, "Scan")
    data object Action : BottomNavItem(Routes.action, Icons.Filled.ArrowCircleRight , "Play")
    data object Profile : BottomNavItem(Routes.profile, Icons.Filled.Person, "Profile")
    data object Settings : BottomNavItem(Routes.settings, Icons.Filled.Settings, "Settings")
}
