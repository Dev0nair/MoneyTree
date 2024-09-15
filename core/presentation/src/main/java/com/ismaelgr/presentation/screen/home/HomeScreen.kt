package com.ismaelgr.presentation.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ismaelgr.domain.model.Result
import com.ismaelgr.presentation.LaunchOnCreate
import com.ismaelgr.presentation.component.MyIcon
import com.ismaelgr.presentation.component.ResultNumbersComponent
import com.ismaelgr.presentation.component.ToolbarComponent
import com.ismaelgr.presentation.screen.loading.LoadingScreen

@Composable
fun HomeScreen(
    goToControlPanel: () -> Unit,
    goToDateDetails: (String) -> Unit,
    goToReport: () -> Unit,
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.resultListState.collectAsState()
    
    LaunchOnCreate {
        viewModel.loadData()
    }
    
    if (state is HomeViewModel.State.Empty) {
        LoadingScreen((state as HomeViewModel.State.Empty).loadingState)
    } else if (state is HomeViewModel.State.Data) {
        View(goToControlPanel = goToControlPanel, goToReport = goToReport, goToDateDetails = goToDateDetails, results = (state as HomeViewModel.State.Data).data)
    }
}

@Composable
private fun View(
    goToControlPanel: () -> Unit,
    goToDateDetails: (String) -> Unit,
    goToReport: () -> Unit,
    results: List<Result>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            ToolbarComponent(title = "Inicio") {
                MyIcon(imageVector = Icons.Default.Info, action = goToReport)
                Spacer(modifier = Modifier.size(10.dp))
                MyIcon(imageVector = Icons.Default.Settings, action = goToControlPanel)
            }
        }
        
        items(results) { result ->
            DateRow(result = result, onDateClick = { goToDateDetails(result.date) })
        }
    }
}

@Composable
private fun DateRow(result: Result, onDateClick: () -> Unit) {
    ResultNumbersComponent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { onDateClick() },
        date = result.date,
        numbers = result.numberList,
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    View(
        {}, {}, {}, listOf(
            Result(date = "20240820", listOf(0, 0, 0, 0, 0, 0)),
            Result(date = "20240819", listOf(19, 2, 30, 41, 18, 6))
        )
    )
}