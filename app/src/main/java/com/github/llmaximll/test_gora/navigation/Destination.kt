package com.github.llmaximll.test_gora.navigation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import com.github.llmaximll.features.home.routeHomeScreen
import com.github.llmaximll.test_gora.R

enum class Destination(
    @StringRes val titleRes: Int,
    val route: String
) {
    Home(
        titleRes = R.string.destination_title_home,
        route = routeHomeScreen
    )
}

@Composable
fun NavDestination?.titleResOrNull(): Int? =
    Destination.entries.find { it.route == this?.route }?.titleRes