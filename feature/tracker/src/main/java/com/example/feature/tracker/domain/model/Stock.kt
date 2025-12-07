package com.example.feature.tracker.domain.model

import java.math.BigDecimal

data class Stock(
    val symbol: String,
    val price: BigDecimal,
    val change: BigDecimal,
    val lastUpdated: Long,
)