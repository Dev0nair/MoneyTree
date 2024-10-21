package com.ismaelgr.presentation.screen.profitstudy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ismaelgr.domain.getFormatedToDate
import com.ismaelgr.domain.getNextDay
import com.ismaelgr.domain.getTodayString
import com.ismaelgr.domain.usecase.CalculateSetsToWinUseCase
import com.ismaelgr.presentation.runUseCase
import com.ismaelgr.presentation.screen.profitstudy.ProfitStudioConfig.MaxTriesPerDay
import com.ismaelgr.presentation.screen.profitstudy.ProfitStudioConfig.MinProfitPerCalc
import com.ismaelgr.presentation.screen.profitstudy.ProfitStudioConfig.NumDaysToCalc
import com.ismaelgr.presentation.toUIDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfitStudioViewModel @Inject constructor(
    private val calculateSetsToWinUseCase: CalculateSetsToWinUseCase
) : ViewModel() {
    
    private val _state: MutableStateFlow<ProfitStudioState> = MutableStateFlow(ProfitStudioState.Loading("Iniciando"))
    val state: StateFlow<ProfitStudioState> = _state
        .onStart { loadData() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProfitStudioState.Loading("Iniciando"))
    
    private fun loadData() {
        val results = mutableListOf<CalculateSetsToWinUseCase.Data.Output>()
        
        processUntil(
            numNextDay = NumDaysToCalc * -1,
            until = -1,
            onLoadData = { newStudy ->
                results.add(newStudy)
            },
            onComplete = {
                updateState(results)
            }
        )
    }
    
    private fun processUntil(numNextDay: Int, onLoadData: (CalculateSetsToWinUseCase.Data.Output) -> Unit, until: Int, onComplete: () -> Unit) {
        val date = getTodayString().getNextDay(numNextDay)
        val flow = calculateSetsToWinUseCase(CalculateSetsToWinUseCase.Data.Input(date = date, profit = MinProfitPerCalc, maxTries = MaxTriesPerDay))
        
        setLoadingMsg("Cargando día ${date.getFormatedToDate().toUIDate()}")
        
        runUseCase(
            flow = flow,
            onEach = onLoadData,
            onComplete = {
                if (numNextDay < until) {
                    processUntil(numNextDay + 1, onLoadData, until, onComplete)
                } else {
                    onComplete()
                }
            }
        )
    }
    
    private fun setLoadingMsg(msg: String) {
        _state.update { ProfitStudioState.Loading(msg) }
    }
    
    private fun updateState(outputs: List<CalculateSetsToWinUseCase.Data.Output>) {
        _state.update { ProfitStudioState.Data(outputs) }
    }
}