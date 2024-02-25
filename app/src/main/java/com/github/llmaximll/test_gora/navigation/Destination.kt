package com.github.llmaximll.test_gora.navigation

import androidx.annotation.StringRes
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