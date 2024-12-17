package com.ismaelgr.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ismaelgr.presentation.screen.controlpanel.useControlPanelScreen
import com.ismaelgr.presentation.screen.datedetails.useDateDetailsScreen
import com.ismaelgr.presentation.screen.daylist.useDayListScreen
import com.ismaelgr.presentation.screen.home.HOME_ROUTE
import com.ismaelgr.presentation.screen.home.useHomeScreen
import com.ismaelgr.presentation.screen.profitstudy.useProfitStudioScreen
import com.ismaelgr.presentation.screen.report.useReportScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

@Composable
fun MainNavigation(navigator: Navigator) {
    val navController = rememberNavController()
    val lifeCycleOwner = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(key1 = lifeCycleOwner) {
        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                navigator.channel.collectLatest { navigatorAction ->
                    when (navigatorAction) {
                        is NavigatorAction.NavigateTo -> navController.navigate(navigatorAction.destination) {
                            navigatorAction.navOptionsBuilder(
                                this
                            )
                        }

                        is NavigatorAction.NavigateUp -> navController.navigateUp()
                    }
                }
            }
        }
    }

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