package com.example.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedTopBar(
    isConnected: Boolean,
    isFeedActive: Boolean,
    onToggleFeed: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        title = { Text("Price Tracker", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isConnected) "🟢" else "🔴",
                    fontSize = 16.sp
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = if (isConnected) "Live" else "Offline",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        },
        actions = {
            IconButton(onClick = onToggleFeed) {
                if (isFeedActive) {
                    Icon(imageVector = Icons.Default.Stop, contentDescription = "Stop Feed")
                } else {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start Feed")
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}
