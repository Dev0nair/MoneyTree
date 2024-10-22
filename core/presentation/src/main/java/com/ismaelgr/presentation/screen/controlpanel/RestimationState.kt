package com.ismaelgr.presentation.screen.controlpanel

sealed interface RestimationState {
    data object Empty : RestimationState
    data object Done : RestimationState
}