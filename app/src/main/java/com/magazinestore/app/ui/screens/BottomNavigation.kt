package com.magazinestore.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.magazinestore.app.data.BottomNavItem

@Composable
fun BottomNavBar() {
    val items = listOf(
        BottomNavItem.Magazine,
        BottomNavItem.Scan,
        BottomNavItem.Action,
        BottomNavItem.Profile,
        BottomNavItem.Settings
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth().height(72.dp),
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = false,
                onClick = {
                    TODO()
                },
                icon = {
                    if (item.route == "Action") {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .height(180.dp)
                                .clip(shape = RoundedCornerShape(50.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .size(180.dp, 180.dp),

                        ) {
//                            Icon(
//                                imageVector = item.icon,
//                                contentDescription = item.label,
//                                tint = MaterialTheme.colorScheme.onSurface,
//                            )
                        }
                    } else {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = if (item.route == "Magazine") MaterialTheme.colorScheme.primary else Color.Gray
                            )
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center,
                                color = if (item.route == "Magazine") MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        }
                    }
                }
            )
        }
    }
}


