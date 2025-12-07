package com.example.feature.tracker.domain.repository

import com.example.feature.tracker.domain.model.Stock
import kotlinx.coroutines.flow.Flow

interface PriceTrackerRepository {
    fun startPriceTracking(symbols: List<String>)
    fun stopPriceTracking()
    fun getPriceUpdates(): Flow<List<Stock>>
}