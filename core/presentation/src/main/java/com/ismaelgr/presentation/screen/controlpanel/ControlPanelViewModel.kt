package com.ismaelgr.presentation.screen.controlpanel

import com.ismaelgr.domain.getNextDay
import com.ismaelgr.domain.getTodayString
import com.ismaelgr.domain.model.EstimatedResult
import com.ismaelgr.domain.usecase.CleanEstimationsUseCase
import com.ismaelgr.domain.usecase.GenerateEstimationOfDateUseCase
import com.ismaelgr.domain.usecase.GenerateStatisticOfDateUseCase
import com.ismaelgr.presentation.navigation.Navigator
import com.ismaelgr.presentation.runUseCase
import com.ismaelgr.presentation.screen.NavigationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ControlPanelViewModel @Inject constructor(
    private val generateEstimationOfDateUseCase: GenerateEstimationOfDateUseCase,
    private val cleanEstimationsUseCase: CleanEstimationsUseCase,
    private val generateStatisticOfDateUseCase: GenerateStatisticOfDateUseCase,
    navigator: Navigator
) : NavigationViewModel(navigator) {
    
    sealed class State {
        data class Empty(val msg: String = "") : State()
        data class Data(val data: List<EstimatedResult>) : State()
    }
    
    sealed class RestimationState {
        data object Empty : RestimationState()
        data object Done : RestimationState()
    }
    
    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Empty())
    val state: StateFlow<State> = _state
    private val _restimationState: MutableStateFlow<RestimationState> = MutableStateFlow(RestimationState.Empty)
    val restimationState: StateFlow<RestimationState> = _restimationState
    
    fun loadData() {
        val daysToCheck = 10
        val daysList: List<String> = ((-1 * daysToCheck)..-1).map { i ->
            getTodayString().getNextDay(i)
        }
        val flows = daysList.map { day -> generateEstimationOfDateUseCase(day) }
        
        setLoadingState("Procesando los últimos $daysToCheck días")
        runUseCase(
            flow = combine(flows) { it },
            onEach = { results ->
                _state.update { State.Data(results.toList()) }
            }
        )
    }
    
    fun restartEstimations() {
        _restimationState.update { RestimationState.Empty }
        runUseCase(
            flow = cleanEstimationsUseCase(),
            onComplete = {
                runUseCase(
                    flow = generateStatisticOfDateUseCase(numDays = 100),
                    onComplete = {
                        _restimationState.update { RestimationState.Done }
                    }
                )
            }
        )
    }
    
    private fun setLoadingState(msg: String) {
        _state.update { State.Empty(msg) }
    }
}