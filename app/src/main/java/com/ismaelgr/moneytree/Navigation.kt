package com.ismaelgr.moneytree

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ismaelgr.presentation.LocalNavController
import com.ismaelgr.presentation.screen.controlpanel.ControlPanelScreen
import com.ismaelgr.presentation.screen.datedetails.useDateDetailsScreen
import com.ismaelgr.presentation.screen.daylist.DayListScreen
import com.ismaelgr.presentation.screen.home.HOME_ROUTE
import com.ismaelgr.presentation.screen.home.HomeScreen
import com.ismaelgr.presentation.screen.profitstudy.ProfitStudioScreen
import com.ismaelgr.presentation.screen.report.ReportScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = HOME_ROUTE) {
            HomeScreen()
            DayListScreen()
            ProfitStudioScreen()
            useDateDetailsScreen()
            ReportScreen()
            ControlPanelScreen()
        }
    }
    
}