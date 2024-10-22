package com.ismaelgr.presentation.screen.profitstudy

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val PROFIT_STUDIO_ROUTE = "profitstudio"

fun NavGraphBuilder.useProfitStudioScreen() {
    composable(route = PROFIT_STUDIO_ROUTE) {
        ProfitStudyScreen()
    }
}