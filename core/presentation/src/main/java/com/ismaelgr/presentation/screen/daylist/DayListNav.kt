package com.ismaelgr.presentation.screen.daylist

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val DAY_LIST_ROUTE = "daylist"

fun NavGraphBuilder.useDayListScreen() {
    composable(route = DAY_LIST_ROUTE) {
        DayListScreen()
    }
}