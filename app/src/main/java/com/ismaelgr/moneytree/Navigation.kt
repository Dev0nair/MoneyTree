package com.ismaelgr.moneytree

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ismaelgr.presentation.LocalNavController
import com.ismaelgr.presentation.screen.controlpanel.useControlPanelScreen
import com.ismaelgr.presentation.screen.datedetails.useDateDetailsScreen
import com.ismaelgr.presentation.screen.daylist.useDayListScreen
import com.ismaelgr.presentation.screen.home.HOME_ROUTE
import com.ismaelgr.presentation.screen.home.useHomeScreen
import com.ismaelgr.presentation.screen.profitstudy.useProfitStudioScreen
import com.ismaelgr.presentation.screen.report.useReportScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = HOME_ROUTE) {
            useHomeScreen()
            useDayListScreen()
            useProfitStudioScreen()
            useDateDetailsScreen()
            useReportScreen()
            useControlPanelScreen()
        }
    }
    
}