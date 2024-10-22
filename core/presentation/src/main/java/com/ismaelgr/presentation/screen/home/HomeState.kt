package com.ismaelgr.presentation.screen.home

import com.ismaelgr.presentation.model.HomeOption

sealed interface HomeState {
    data object Loading : HomeState
    data class Data(val options: List<HomeOption>) : HomeState
}