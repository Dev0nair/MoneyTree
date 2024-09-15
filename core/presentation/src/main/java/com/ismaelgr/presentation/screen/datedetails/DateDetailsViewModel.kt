package com.ismaelgr.presentation.screen.datedetails

import androidx.lifecycle.ViewModel
import com.ismaelgr.domain.model.EstimatedResult
import com.ismaelgr.domain.usecase.GenerateEstimationOfDateUseCase
import com.ismaelgr.presentation.runUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DateDetailsViewModel @Inject constructor(
    private val generateEstimationOfDateUseCase: GenerateEstimationOfDateUseCase
) : ViewModel() {
    
    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Empty())
    val state: StateFlow<State> = _state
    
    fun loadData(date: String) {
        setLoadingState("Loading data")
        runUseCase(
            flow = generateEstimationOfDateUseCase(date),
            onEach = { currentResults ->
                _state.update { State.Data(currentResults) }
            }
        )
    }
    
    private fun setLoadingState(msg: String) {
        _state.update { State.Empty(msg) }
    }
    
    sealed class State {
        data class Empty(val loadingMsg: String = "") : State()
        data class Data(val estimatedResult: EstimatedResult) : State()
    }
}