package com.example.core.navigation

sealed class Screen(val route: String) {
    data object Feed : Screen("feed")
    data object Details : Screen("details/{symbol}") {
        fun createRoute(symbol: String) = "details/$symbol"
    }
}