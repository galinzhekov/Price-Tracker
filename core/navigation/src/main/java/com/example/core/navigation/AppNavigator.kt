package com.example.core.navigation

import kotlinx.coroutines.flow.SharedFlow

interface AppNavigator {
    val commands: SharedFlow<NavigationCommand>
    suspend fun navigate(command: NavigationCommand)
}

sealed class NavigationCommand {
    data class ToDetails(val symbol: String) : NavigationCommand()
    data object GoBack : NavigationCommand()
}