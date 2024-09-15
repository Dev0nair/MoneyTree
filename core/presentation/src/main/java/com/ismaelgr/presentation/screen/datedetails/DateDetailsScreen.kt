package com.ismaelgr.presentation.screen.datedetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ismaelgr.domain.getFormatedToDate
import com.ismaelgr.domain.model.EstimatedResult
import com.ismaelgr.presentation.component.DateStatistics
import com.ismaelgr.presentation.component.ResultNumbersComponent
import com.ismaelgr.presentation.component.ToolbarComponent
import com.ismaelgr.presentation.screen.loading.LoadingScreen
import com.ismaelgr.presentation.toUIDate

@Composable
fun DateDetailsScreen(
    date: String,
    goBack: () -> Unit
) {
    val viewModel: DateDetailsViewModel = hiltViewModel()
    val state: DateDetailsViewModel.State by viewModel.state.collectAsState()
    
    LaunchedEffect(key1 = Unit) {
        viewModel.loadData(date)
    }
    
    if (state is DateDetailsViewModel.State.Empty) {
        LoadingScreen(loadingMsg = (state as DateDetailsViewModel.State.Empty).loadingMsg)
    } else {
        View(goBack = goBack, estimationResult = (state as DateDetailsViewModel.State.Data).estimatedResult)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun View(goBack: () -> Unit, estimationResult: EstimatedResult) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        stickyHeader {
            ToolbarComponent(title = estimationResult.date.getFormatedToDate().toUIDate(), goBackClick = goBack)
        }
        
        item {
            ResultNumbersComponent(date = "Resultados finales", numbers = estimationResult.winnerList, showDate = false)
        }
        
        item {
            DateStatistics(estimatedResult = estimationResult)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    View({}, EstimatedResult("20240819", estimations = emptyList(), statistic = emptyList(), winnerList = emptyList()))
}