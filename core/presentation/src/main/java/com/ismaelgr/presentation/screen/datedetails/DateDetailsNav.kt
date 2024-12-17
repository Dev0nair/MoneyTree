package com.ismaelgr.presentation.screen.datedetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val DATE_DETAILS_ROUTE = "dateDetails/{date}"
fun getDateDetailsRoute(date: String) = "dateDetails/$date"

fun NavGraphBuilder.useDateDetailsScreen() {
    composable(route = DATE_DETAILS_ROUTE) {
        DateDetailsScreen()
    }
}