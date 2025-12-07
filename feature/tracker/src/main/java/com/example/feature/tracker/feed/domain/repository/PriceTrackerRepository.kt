package com.example.feature.tracker.feed.domain.repository

import com.example.feature.tracker.feed.domain.model.Stock
import kotlinx.coroutines.flow.Flow

interface PriceTrackerRepository {
    fun startPriceTracking(symbols: List<String>)
    fun stopPriceTracking()
    fun getPriceUpdates(): Flow<List<Stock>>
}