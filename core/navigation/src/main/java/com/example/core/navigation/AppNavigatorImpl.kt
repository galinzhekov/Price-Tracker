package com.example.core.navigation

import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigatorImpl @Inject constructor() : AppNavigator {
    private val _commands = MutableSharedFlow<NavigationCommand>(extraBufferCapacity = 1)
    override val commands = _commands.asSharedFlow()

    override suspend fun navigate(command: NavigationCommand) {
        _commands.emit(command)
    }
}

fun handleNavigation(navController: NavController, command: NavigationCommand) {
    when (command) {
        is NavigationCommand.ToDetails -> navController.navigate("details/${command.symbol}")
        NavigationCommand.GoBack -> navController.popBackStack()
    }
}
