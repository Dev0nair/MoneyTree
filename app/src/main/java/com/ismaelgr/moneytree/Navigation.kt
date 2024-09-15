package com.ismaelgr.moneytree

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ismaelgr.presentation.screen.controlpanel.ControlPanelScreen
import com.ismaelgr.presentation.screen.datedetails.DateDetailsScreen
import com.ismaelgr.presentation.screen.home.HomeScreen
import com.ismaelgr.presentation.screen.report.ReportScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "home") {
        composable(route = "home") {
            HomeScreen(
                goToControlPanel = {
                    navController.navigate("controlpanel")
                },
                goToDateDetails = { date ->
                    navController.navigate("dateDetails/${date}")
                },
                goToReport = {
                    navController.navigate("report")
                }
            )
        }
        
        composable(
            route = "dateDetails/{date}",
            arguments = listOf(navArgument("date") { type = NavType.StringType })
        ) {
            val date: String? = it.arguments?.getString("date")
            
            if (date == null) {
                navController.navigateUp()
            } else {
                DateDetailsScreen(date = date, goBack = { navController.navigateUp() })
            }
        }
        
        composable(route = "report") {
            ReportScreen(
                goBack = navController::navigateUp
            )
        }
        
        composable(route = "controlpanel") {
            ControlPanelScreen(
                goBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}