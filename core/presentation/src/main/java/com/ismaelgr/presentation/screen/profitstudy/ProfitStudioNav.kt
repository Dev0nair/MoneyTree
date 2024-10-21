package com.ismaelgr.presentation.screen.profitstudy

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ismaelgr.presentation.currentNavController

const val PROFIT_STUDIO_ROUTE = "profitstudio"

fun NavGraphBuilder.useProfitStudioScreen() {
    composable(route = PROFIT_STUDIO_ROUTE) {
        val navController = currentNavController()
        
        ProfitStudyScreen(
            goBack = navController::navigateUp
        )
    }
}