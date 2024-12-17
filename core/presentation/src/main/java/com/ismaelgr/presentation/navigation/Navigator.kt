package com.ismaelgr.presentation.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.Flow

interface Navigator {
    val channel: Flow<NavigatorAction>
    fun navigate(destination: String, navOptionsBuilder: NavOptionsBuilder.() -> Unit = {})
    fun navigateUp()
}