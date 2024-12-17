package com.ismaelgr.presentation.screen.report

import androidx.lifecycle.viewModelScope
import com.ismaelgr.domain.getNextDay
import com.ismaelgr.domain.getTodayString
import com.ismaelgr.domain.model.EstimatedResult
import com.ismaelgr.domain.toPrize
import com.ismaelgr.domain.usecase.GenerateEstimationOfDateUseCase
import com.ismaelgr.presentation.model.ReportData
import com.ismaelgr.presentation.navigation.Navigator
import com.ismaelgr.presentation.runUseCase
import com.ismaelgr.presentation.screen.NavigationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val generateEstimationOfDateUseCase: GenerateEstimationOfDateUseCase,
    navigator: Navigator
) : NavigationViewModel(navigator) {

    private val _state: MutableStateFlow<ReportState> = MutableStateFlow(ReportState.Empty())
    val state: StateFlow<ReportState> = _state
        .onStart { loadData() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, _state.value)


    private fun loadData() {
        setLoadingState("Procesando los últimos ${ReportConfig.DAYS_TO_CHECK} días")

        val daysList: List<String> = ((-1 * ReportConfig.DAYS_TO_CHECK)..-1).map { i ->
            getTodayString().getNextDay(i)
        }
        val flows = daysList.map { day -> generateEstimationOfDateUseCase(day) }

        runUseCase(
            flow = combine(flows) { resultList -> resultList.map(::mapToReportData) },
            onEach = { results ->
                _state.update { ReportState.Data(results.sortedByDescending { it.date }) }
            }
        )
    }

    private fun mapToReportData(estimatedResult: EstimatedResult): ReportData {
        val estimationWithRewards: List<List<Int>> = estimatedResult.estimations
            .filter { estimation ->
                estimation.filter { numberData -> numberData.number in estimatedResult.winnerList }.size > 2
            }
            .map { estimation -> estimation.map { numberData -> numberData.number } }
        val finalReward: Int =
            estimationWithRewards.map { estimation -> estimation.filter { number -> number in estimatedResult.winnerList }.size }
                .sumOf(Int::toPrize)


        return ReportData(
            date = estimatedResult.date,
            winnerList = estimatedResult.winnerList,
            estimationsWithReward = estimationWithRewards,
            estimatedReward = finalReward
        )
    }

    private fun setLoadingState(msg: String) {
        _state.update { ReportState.Empty(msg) }
    }

}