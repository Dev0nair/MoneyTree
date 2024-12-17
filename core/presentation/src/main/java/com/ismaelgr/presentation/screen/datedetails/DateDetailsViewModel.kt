package com.ismaelgr.presentation.screen.datedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ismaelgr.domain.usecase.GenerateEstimationOfDateUseCase
import com.ismaelgr.presentation.navigation.Navigator
import com.ismaelgr.presentation.runUseCase
import com.ismaelgr.presentation.screen.NavigationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DateDetailsViewModel @Inject constructor(
    private val generateEstimationOfDateUseCase: GenerateEstimationOfDateUseCase,
    private val savedStateHandle: SavedStateHandle,
    navigator: Navigator
) : NavigationViewModel(navigator) {

    private val _state: MutableStateFlow<DateDetailsState> =
        MutableStateFlow(DateDetailsState.Empty())
    val state: StateFlow<DateDetailsState> = _state
        .onStart { loadData() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, DateDetailsState.Empty())

    private fun loadData() {
        val dateFromParam: String? = savedStateHandle.get<String>("date")

        if (dateFromParam != null) {
            setLoadingState("Loading data")
            runUseCase(
                flow = generateEstimationOfDateUseCase(dateFromParam),
                onEach = { currentResults ->
                    _state.update { DateDetailsState.Data(currentResults) }
                }
            )
        } else {
            _state.update { DateDetailsState.Error(msg = "Date not found") }
        }
    }

    private fun setLoadingState(msg: String) {
        _state.update { DateDetailsState.Empty(msg) }
    }
}