package com.example.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PriceChangeIndicator(change: BigDecimal) {
    val priceFormatter = rememberPriceFormatter()
    val (indicator, color) = when {
        change > BigDecimal.ZERO -> "↑" to Color(0xFF2E7D32)
        change < BigDecimal.ZERO -> "↓" to Color(0xFFC62828)
        else -> "" to MaterialTheme.colorScheme.onSurface
    }

    if (indicator.isNotEmpty()) {
        Row {
            Text(
                text = indicator,
                color = color,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = priceFormatter.format(change.abs()),
                color = color,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun rememberPriceFormatter(): NumberFormat {
    return remember {
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    }
}
