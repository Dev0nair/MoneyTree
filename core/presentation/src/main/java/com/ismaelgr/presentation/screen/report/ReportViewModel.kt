package com.ismaelgr.presentation.screen.report

import androidx.lifecycle.ViewModel
import com.ismaelgr.domain.getNextDay
import com.ismaelgr.domain.getTodayString
import com.ismaelgr.domain.model.EstimatedResult
import com.ismaelgr.domain.usecase.GenerateEstimationOfDateUseCase
import com.ismaelgr.presentation.model.ReportData
import com.ismaelgr.presentation.runUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val generateEstimationOfDateUseCase: GenerateEstimationOfDateUseCase,
) : ViewModel() {
    
    sealed class State {
        data class Empty(val msg: String = "") : State()
        data class Data(val data: List<ReportData>) : State()
    }
    
    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Empty())
    val state: StateFlow<State> = _state
    
    fun loadData() {
        val daysToCheck = 30
        val daysList: List<String> = ((-1 * daysToCheck)..-1).map { i ->
            getTodayString().getNextDay(i)
        }
        val flows = daysList.map { day -> generateEstimationOfDateUseCase(day) }
        
        setLoadingState("Procesando los últimos $daysToCheck días")
        
        runUseCase(
            flow = combine(flows) { resultList -> resultList.map(::mapToReportData) },
            onEach = { results ->
                _state.update { State.Data(results) }
            }
        )
    }
    
    private fun mapToReportData(estimatedResult: EstimatedResult): ReportData {
        val estimationWithRewards: List<List<Int>> = estimatedResult.estimations
            .filter { estimation ->
                estimation.filter { numberData -> numberData.number in estimatedResult.winnerList }.size > 2
            }
            .map { estimation -> estimation.map { numberData -> numberData.number } }
        val finalReward: Int = estimationWithRewards.map { estimation -> estimation.filter { number -> number in estimatedResult.winnerList }.size }
            .map { count ->
                when (count) {
                    3 -> 4
                    4 -> 25
                    5 -> 1_000
                    else -> 0
                }
            }.sum()
        
        
        return ReportData(
            date = estimatedResult.date,
            winnerList = estimatedResult.winnerList,
            estimationsWithReward = estimationWithRewards,
            estimatedReward = finalReward
        )
    }
    
    private fun setLoadingState(msg: String) {
        _state.update { State.Empty(msg) }
    }
    
}