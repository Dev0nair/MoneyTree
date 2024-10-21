package com.ismaelgr.presentation.screen.datedetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ismaelgr.presentation.currentNavController

const val DATE_DETAILS_ROUTE = "dateDetails/{date}"

fun NavGraphBuilder.useDateDetailsScreen() {
    composable(route = DATE_DETAILS_ROUTE) {
        val navController = currentNavController()
        DateDetailsScreen(goBack = { navController.navigateUp() })
    }
}