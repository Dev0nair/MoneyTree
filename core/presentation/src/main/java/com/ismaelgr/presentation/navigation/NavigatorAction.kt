package com.ismaelgr.presentation.navigation

import androidx.navigation.NavOptionsBuilder

sealed interface NavigatorAction {
    data class NavigateTo(
        val destination: String,
        val navOptionsBuilder: NavOptionsBuilder.() -> Unit
    ) : NavigatorAction

    data object NavigateUp : NavigatorAction
}