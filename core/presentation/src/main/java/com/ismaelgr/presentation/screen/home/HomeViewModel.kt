package com.ismaelgr.presentation.screen.home

import androidx.lifecycle.viewModelScope
import com.ismaelgr.domain.usecase.DownloadDataUseCase
import com.ismaelgr.presentation.model.HomeOption
import com.ismaelgr.presentation.navigation.Navigator
import com.ismaelgr.presentation.runUseCase
import com.ismaelgr.presentation.screen.NavigationViewModel
import com.ismaelgr.presentation.screen.controlpanel.CONTROL_PANEL_ROUTE
import com.ismaelgr.presentation.screen.daylist.DAY_LIST_ROUTE
import com.ismaelgr.presentation.screen.profitstudy.PROFIT_STUDIO_ROUTE
import com.ismaelgr.presentation.screen.report.REPORT_ROUTE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val downloadDataUseCase: DownloadDataUseCase,
    navigator: Navigator
) : NavigationViewModel(navigator) {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    val state: StateFlow<HomeState> = _state
        .onStart { downloadData() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, HomeState.Loading)

    private val optionList = listOf(
        HomeOption("Lista de días") { navigate(DAY_LIST_ROUTE) },
        HomeOption("Estudio resultados") { navigate(DAY_LIST_ROUTE) },
        HomeOption("Estudio ganancias") { navigate(PROFIT_STUDIO_ROUTE) },
        HomeOption("Resultados") { navigate(DAY_LIST_ROUTE) },
        HomeOption("Panel de control") { navigate(CONTROL_PANEL_ROUTE) },
        HomeOption("Report") { navigate(REPORT_ROUTE) }
    )

    private fun downloadData() {
        runUseCase(
            flow = downloadDataUseCase(),
            onComplete = {
                _state.update { HomeState.Data(optionList) }
            }
        )
    }
}