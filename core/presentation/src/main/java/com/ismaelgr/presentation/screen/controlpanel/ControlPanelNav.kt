package com.ismaelgr.presentation.screen.controlpanel

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val CONTROL_PANEL_ROUTE = "controlpanel"

fun NavGraphBuilder.useControlPanelScreen() {
    composable(route = CONTROL_PANEL_ROUTE) {
        ControlPanelScreen()
    }
}