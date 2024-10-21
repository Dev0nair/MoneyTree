package com.ismaelgr.presentation.screen.controlpanel

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ismaelgr.presentation.currentNavController

const val CONTROL_PANEL_ROUTE = "controlpanel"

fun NavGraphBuilder.useControlPanelScreen() {
    composable(route = CONTROL_PANEL_ROUTE) {
        val navController = currentNavController()
        ControlPanelScreen(
            goBack = navController::navigateUp
        )
    }
}