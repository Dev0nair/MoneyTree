package com.ismaelgr.presentation.screen.report

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val REPORT_ROUTE = "report"

fun NavGraphBuilder.useReportScreen() {
    composable(route = REPORT_ROUTE) {
        ReportScreen()
    }
}