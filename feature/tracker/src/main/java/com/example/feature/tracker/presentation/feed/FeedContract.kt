package com.example.feature.tracker.presentation.feed

import com.example.feature.tracker.domain.model.Stock

sealed class FeedEvent {
    data class OnSymbolClicked(val symbol: String) : FeedEvent()
    data object OnToggleFeed : FeedEvent()
}

data class FeedState(
    val stocks: List<Stock> = emptyList(),
    val isConnected: Boolean = false,
    val isFeedActive: Boolean = false
)