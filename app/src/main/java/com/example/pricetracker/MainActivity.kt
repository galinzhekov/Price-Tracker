package com.example.pricetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.core.navigation.AppNavigator
import com.example.feature.tracker.presentation.feed.FeedScreen
import com.example.feature.tracker.presentation.feed.FeedViewModel
import com.example.core.navigation.Screen
import com.example.core.navigation.handleNavigation
import com.example.feature.tracker.presentation.details.DetailsScreen
import com.example.feature.tracker.presentation.details.DetailsViewModel
import com.example.pricetracker.ui.theme.PriceTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PriceTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(appNavigator)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    appNavigator: AppNavigator
) {
    val navController: NavHostController = rememberNavController()

    LaunchedEffect(Unit) {
        appNavigator.commands.collect { command ->
            handleNavigation(navController, command)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Feed.route
    ) {
        composable(route = Screen.Feed.route) {
            val viewModel: FeedViewModel = hiltViewModel()
            val uiState by viewModel.state.collectAsStateWithLifecycle()
            FeedScreen(
                state = uiState,
                actions = viewModel::dispatch,
            )
        }

        composable(route = Screen.Details.route,
        ) {
            val viewModel: DetailsViewModel = hiltViewModel()
            val uiState by viewModel.state.collectAsStateWithLifecycle()
            DetailsScreen(
                state = uiState,
                actions = viewModel::dispatch,
            )
        }
    }
}