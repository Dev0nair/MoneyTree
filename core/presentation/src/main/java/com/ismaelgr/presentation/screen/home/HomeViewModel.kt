package com.ismaelgr.presentation.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ismaelgr.domain.getNextDay
import com.ismaelgr.domain.getTodayString
import com.ismaelgr.domain.model.Result
import com.ismaelgr.domain.usecase.DownloadDataUseCase
import com.ismaelgr.domain.usecase.GenerateStatisticOfDateUseCase
import com.ismaelgr.domain.usecase.GetResultsUseCase
import com.ismaelgr.presentation.runUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val downloadDataUseCase: DownloadDataUseCase,
    private val generateStatisticOfDateUseCase: GenerateStatisticOfDateUseCase,
    private val getResultsUseCase: GetResultsUseCase
) : ViewModel() {
    
    sealed class State {
        data class Empty(val loadingState: String = "") : State()
        data class Data(val data: List<Result>) : State()
    }
    
    private val numDays: Int = 10
    private val _resultListState: MutableStateFlow<State> = MutableStateFlow(State.Empty())
    val resultListState: StateFlow<State> = _resultListState
    
    fun loadData() {
        setLoadingState("Downloading new data")
        downloadDataUntilToday(
            onComplete = {
                setLoadingState("Generating statistic")
                generateStatistics(
                    onComplete = {
                        setLoadingState("Loading results")
                        loadResults()
                    })
            }
        )
    }
    
    private fun setLoadingState(msg: String) {
        _resultListState.update { State.Empty(msg) }
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
                    
                    _resultListState.update { State.Data(newList) }
                } else {
                    _resultListState.update { State.Data(list) }
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
    
    private fun downloadDataUntilToday(onComplete: () -> Unit) {
        runUseCase(
            flow = downloadDataUseCase.retrieveUntil(getTodayString()),
            onComplete = onComplete
        )
    }
}
