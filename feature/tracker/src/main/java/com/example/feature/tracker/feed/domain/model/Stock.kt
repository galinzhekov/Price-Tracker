package com.example.feature.tracker.feed.domain.model

import java.math.BigDecimal

data class Stock(
    val symbol: String,
    val price: BigDecimal,
    val change: BigDecimal,
    val lastUpdated: Long,
)