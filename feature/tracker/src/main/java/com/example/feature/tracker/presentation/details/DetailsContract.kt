package com.example.feature.tracker.presentation.details

import com.example.feature.tracker.domain.model.Stock

sealed class DetailsEvent {
    data object OnBackClicked : DetailsEvent()
}

data class DetailsState(
    val stock: Stock? = null,
    val description: String = ""
)