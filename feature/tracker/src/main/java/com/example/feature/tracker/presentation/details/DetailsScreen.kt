package com.example.feature.tracker.presentation.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.core.ui.components.PriceChangeIndicator
import com.example.core.ui.components.rememberPriceFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    state: DetailsState,
    actions: (DetailsEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.stock?.symbol ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = { actions(DetailsEvent.OnBackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.stock == null) {
                CircularProgressIndicator()
            } else {
                val priceFormatter = rememberPriceFormatter()

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = state.stock.symbol,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = priceFormatter.format(state.stock.price),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    PriceChangeIndicator(change = state.stock.change)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = state.description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}