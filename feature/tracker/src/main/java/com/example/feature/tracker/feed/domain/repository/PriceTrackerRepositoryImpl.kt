package com.example.feature.tracker.feed.domain.repository

import com.example.feature.tracker.feed.data.remote.WebSocketEvent
import com.example.feature.tracker.feed.data.remote.WebSocketService
import com.example.feature.tracker.feed.domain.model.Stock
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

data class PriceUpdate(val symbol: String, val price: Double)

@Singleton
class PriceTrackerRepositoryImpl @Inject constructor(
    private val webSocketService: WebSocketService,
    private val gson: Gson
) : PriceTrackerRepository {

    private val priceDataFlow = MutableStateFlow<Map<String, Stock>>(emptyMap())
    private val repositoryScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var priceUpdateJob: Job? = null

    init {
        repositoryScope.launch {
            webSocketService.events.collect { event ->
                if (event is WebSocketEvent.Message) {
                    try {
                        val update = gson.fromJson(event.text, PriceUpdate::class.java)
                        updatePrice(update.symbol, BigDecimal(update.price))
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }

    override fun startPriceTracking(symbols: List<String>) {
        if (priceDataFlow.value.isEmpty()) {
            val initialPrices = symbols.associateWith { symbol ->
                Stock(
                    symbol = symbol,
                    price = BigDecimal(Random.nextDouble(100.0, 500.0)).setScale(2, RoundingMode.HALF_UP),
                    change = BigDecimal.ZERO,
                    lastUpdated = 0L
                )
            }
            priceDataFlow.value = initialPrices
        }

        if (priceUpdateJob?.isActive != true) {
            priceUpdateJob = repositoryScope.launch {
                while (true) {
                    delay(2000)
                    priceDataFlow.value.keys.forEach { symbol ->
                        sendRandomPriceUpdate(symbol)
                    }
                }
            }
        }
    }

    private suspend fun sendRandomPriceUpdate(symbol: String) {
        val oldPrice = priceDataFlow.value[symbol]?.price ?: return
        val priceChange = BigDecimal(Random.nextDouble(-5.0, 5.0))
        val newPrice = (oldPrice + priceChange).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP)
        val updateMessage = gson.toJson(PriceUpdate(symbol, newPrice.toDouble()))
        webSocketService.sendMessage(updateMessage)
    }

    private fun updatePrice(symbol: String, newPrice: BigDecimal) {
        priceDataFlow.update { currentMap ->
            val oldStock = currentMap[symbol] ?: return@update currentMap
            val priceChange = newPrice - oldStock.price
            val updatedStock = oldStock.copy(
                price = newPrice,
                change = priceChange,
                lastUpdated = System.currentTimeMillis()
            )
            currentMap + (symbol to updatedStock)
        }
    }

    override fun stopPriceTracking() {
        priceUpdateJob?.cancel()
        priceUpdateJob = null
    }

    override fun getPriceUpdates(): Flow<List<Stock>> {
        return priceDataFlow.map { it.values.sortedByDescending { stock -> stock.price } }
    }
}
