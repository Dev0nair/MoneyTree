package com.ismaelgr.presentation.screen.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ismaelgr.presentation.currentNavController

const val HOME_ROUTE = "home"

fun NavGraphBuilder.useHomeScreen() {
    composable(route = HOME_ROUTE) {
        val navController = currentNavController()
        
        HomeScreen(navigateTo = { route -> navController.navigate(route) })
    }
}