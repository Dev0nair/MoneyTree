package com.ismaelgr.presentation.screen.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val HOME_ROUTE = "home"

fun NavGraphBuilder.useHomeScreen() {
    composable(route = HOME_ROUTE) {
        HomeScreen()
    }
}