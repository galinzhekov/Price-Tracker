package com.example.feature.tracker.feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.navigation.AppNavigator
import com.example.core.navigation.NavigationCommand
import com.example.feature.tracker.feed.domain.repository.PriceTrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val priceTrackerRepository: PriceTrackerRepository,
    private val appNavigator: AppNavigator
) : ViewModel() {

    private val _state = MutableStateFlow(FeedState())
    val state = _state.asStateFlow()

    private val stockSymbols = listOf(
        "AAPL", "GOOG", "TSLA", "AMZN", "MSFT", "NVDA", "META", "NFLX", "ADBE", "ORCL",
        "CSCO", "INTC", "QCOM", "AMD", "PYPL", "CRM", "IBM", "UBER", "ZM", "SQ",
        "SNAP", "PINS", "TWTR", "ETSY", "SHOP"
    )

    init {
        dispatch(FeedEvent.OnToggleFeed)
    }

    fun dispatch(event: FeedEvent) {
        when (event) {
            is FeedEvent.OnToggleFeed -> {
                toggleFeed()
            }

            is FeedEvent.OnSymbolClicked -> {
                viewModelScope.launch {
                    appNavigator.navigate(NavigationCommand.ToDetails(event.symbol))
                }
            }
        }
    }

    private fun toggleFeed() {
        val isCurrentlyActive = _state.value.isFeedActive
        _state.update { it.copy(isFeedActive = !isCurrentlyActive) }

        if (!isCurrentlyActive) {
            priceTrackerRepository.startPriceTracking(stockSymbols)
            startObservingPrices()
        } else {
            priceTrackerRepository.stopPriceTracking()
            _state.update { it.copy(isConnected = false, stocks = emptyList()) }
        }
    }

    private fun startObservingPrices() {
        priceTrackerRepository.getPriceUpdates()
            .distinctUntilChanged()
            .onEach { updatedStocks ->
                _state.update {
                    it.copy(
                        stocks = updatedStocks,
                        isConnected = updatedStocks.isNotEmpty()
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}
