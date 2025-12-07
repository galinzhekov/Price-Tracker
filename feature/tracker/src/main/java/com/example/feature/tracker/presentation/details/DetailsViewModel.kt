package com.example.feature.tracker.presentation.details

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.core.navigation.AppNavigator
import com.example.core.navigation.NavigationCommand
import com.example.feature.tracker.domain.repository.PriceTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val priceTrackerRepository: PriceTrackerRepository,
    private val appNavigator: AppNavigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()

    private val symbol: String = getSymbolFromStateHandle(savedStateHandle)

    init {
        _state.update {
            it.copy(
                description = "This is a sample description for $symbol. In a real app, this would be fetched from an API. $symbol is a leading company in its industry, known for innovation and market leadership."
            )
        }
        observePriceUpdates()
    }

    private fun getSymbolFromStateHandle(handle: SavedStateHandle): String {
        val navArgSymbol: String? = handle.get<String>("symbol")
        if (navArgSymbol != null) {
            return navArgSymbol
        }

        val deepLinkIntent: Intent? = handle.get(NavController.KEY_DEEP_LINK_INTENT)
        val deepLinkUri = deepLinkIntent?.data
        val deepLinkSymbol = deepLinkUri?.lastPathSegment
        if (deepLinkSymbol != null) {
            return deepLinkSymbol
        }

        return "Unknown"
    }

    private fun observePriceUpdates() {
        priceTrackerRepository.getPriceUpdates()
            .onEach { allStocks ->
                val stockDetails = allStocks.find { it.symbol == symbol }
                if (stockDetails != null) {
                    _state.update { it.copy(stock = stockDetails) }
                }
            }
            .launchIn(viewModelScope)
    }

    fun dispatch(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.OnBackClicked -> {
                viewModelScope.launch {
                    appNavigator.navigate(NavigationCommand.GoBack)
                }
            }
        }
    }
}