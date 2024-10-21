package com.ismaelgr.presentation.screen.daylist

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ismaelgr.presentation.currentNavController

const val DAY_LIST_ROUTE = "daylist"

fun NavGraphBuilder.useDayListScreen() {
    composable(route = DAY_LIST_ROUTE) {
        val navController = currentNavController()
        
        DayListScreen(goBack = navController::navigateUp, goToDateDetails = {
            navController.navigate("dateDetails/$it")
        })
    }
}