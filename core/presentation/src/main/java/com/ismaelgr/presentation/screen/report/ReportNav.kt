package com.ismaelgr.presentation.screen.report

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ismaelgr.presentation.currentNavController

const val REPORT_ROUTE = "report"

fun NavGraphBuilder.ReportScreen() {
    composable(route = REPORT_ROUTE) {
        val navController = currentNavController()
        ReportScreen(
            goBack = navController::navigateUp
        )
    }
}