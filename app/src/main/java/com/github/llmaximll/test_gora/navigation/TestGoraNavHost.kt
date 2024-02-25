package com.github.llmaximll.test_gora.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.llmaximll.features.home.HomeScreen

@Composable
fun TestGoraNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onArticleClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Destination.Home.route
    ) {
        composable(
            route = Destination.Home.route
        ) {
            HomeScreen(
                onArticleClick = onArticleClick,
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}