package com.ismaelgr.presentation.screen.daylist

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ismaelgr.domain.getNextDay
import com.ismaelgr.domain.model.Result
import com.ismaelgr.domain.usecase.GenerateStatisticOfDateUseCase
import com.ismaelgr.domain.usecase.GetResultsUseCase
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
class DayListViewModel @Inject constructor(
    private val generateStatisticOfDateUseCase: GenerateStatisticOfDateUseCase,
    private val getResultsUseCase: GetResultsUseCase,
    navigator: Navigator
) : NavigationViewModel(navigator) {

    private val numDays: Int = 10
    private val _resultListState: MutableStateFlow<DayListState> =
        MutableStateFlow(DayListState.Empty())
    val resultListState: StateFlow<DayListState> = _resultListState
        .onStart {
            loadData()
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, DayListState.Empty())

    private fun loadData() {
        setLoadingState("Generating statistic")
        generateStatistics(
            onComplete = {
                setLoadingState("Loading results")
                loadResults()
            })
    }

    private fun setLoadingState(msg: String) {
        _resultListState.update { DayListState.Empty(msg) }
        Log.i("HomeViewModel", msg)
    }

    private fun loadResults() {
        runUseCase(
            flow = getResultsUseCase(numDays),
            onEach = { list ->
                if (list.isNotEmpty()) {
                    val newResult = Result(
                        date = list.first().date.getNextDay(1),
                        numberList = listOf(0, 0, 0, 0, 0, 0)
                    )
                    val newList: List<Result> = mutableListOf(newResult, *list.toTypedArray())

                    _resultListState.update { DayListState.Data(newList) }
                } else {
                    _resultListState.update { DayListState.Data(list) }
                }
            }
        )
    }

    private fun generateStatistics(onComplete: () -> Unit) {
        runUseCase(
            flow = generateStatisticOfDateUseCase(numDays),
            onComplete = onComplete
        )
    }
}
