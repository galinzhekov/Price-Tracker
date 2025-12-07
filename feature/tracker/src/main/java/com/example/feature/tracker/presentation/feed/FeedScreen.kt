package com.example.feature.tracker.presentation.feed

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.core.ui.components.FeedTopBar
import com.example.core.ui.components.PriceChangeIndicator
import com.example.core.ui.components.rememberPriceFormatter
import com.example.feature.tracker.domain.model.Stock
import kotlinx.coroutines.delay
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    state: FeedState,
    actions: (FeedEvent) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FeedTopBar(
                isConnected = state.isConnected,
                isFeedActive = state.isFeedActive,
                onToggleFeed = { actions(FeedEvent.OnToggleFeed) },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (state.stocks.isEmpty() && state.isFeedActive) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Connecting to feed...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                StockList(
                    stocks = state.stocks,
                    onSymbolClicked = { actions(FeedEvent.OnSymbolClicked(it)) }
                )
            }
        }
    }
}

@Composable
fun StockList(
    stocks: List<Stock>,
    onSymbolClicked: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = stocks,
            key = { stock -> stock.symbol }
        ) { stock ->
            StockRow(stock = stock, onClick = { onSymbolClicked(stock.symbol) })
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        }
    }
}

@Composable
fun StockRow(
    stock: Stock,
    onClick: () -> Unit
) {
    val priceFormatter = rememberPriceFormatter()

    var flashState by remember { mutableStateOf(FlashState.TRANSPARENT) }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = when (flashState) {
            FlashState.TRANSPARENT -> Color.Transparent
            FlashState.GREEN -> Color(0xFF2E7D32).copy(alpha = 0.3f)
            FlashState.RED -> Color(0xFFC62828).copy(alpha = 0.3f)
        },
        animationSpec = tween(durationMillis = 200),
        label = "Row Background Color"
    )

    LaunchedEffect(stock.price) {
        if (stock.change != BigDecimal.ZERO) {
            flashState = if (stock.change > BigDecimal.ZERO) FlashState.GREEN else FlashState.RED
            delay(1000)
            flashState = FlashState.TRANSPARENT
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(animatedBackgroundColor, shape = MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stock.symbol,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${stock.symbol} Inc.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = priceFormatter.format(stock.price),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            PriceChangeIndicator(change = stock.change)
        }
    }
}

private enum class FlashState {
    TRANSPARENT,
    GREEN,
    RED
}