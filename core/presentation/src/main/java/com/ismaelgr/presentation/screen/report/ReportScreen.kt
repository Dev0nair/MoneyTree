package com.ismaelgr.presentation.screen.report

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ismaelgr.domain.getFormatedToDate
import com.ismaelgr.domain.getNextDay
import com.ismaelgr.domain.getTodayString
import com.ismaelgr.presentation.LaunchOnCreate
import com.ismaelgr.presentation.component.ToolbarComponent
import com.ismaelgr.presentation.model.ReportData
import com.ismaelgr.presentation.screen.loading.LoadingScreen
import com.ismaelgr.presentation.toUIDate

@Composable
fun ReportScreen() {
    val viewModel: ReportViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    
    LaunchOnCreate {
        viewModel.loadData()
    }
    
    if (state is ReportViewModel.State.Empty) {
        LoadingScreen(loadingMsg = (state as ReportViewModel.State.Empty).msg)
    } else {
        View(viewModel::navigateUp, (state as ReportViewModel.State.Data).data)
    }
    
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun View(goBack: () -> Unit, data: List<ReportData>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        stickyHeader { ToolbarComponent(title = "Report Screen", goBackClick = goBack) }
        
        item {
            val avg = data.map { report -> report.estimationsWithReward.size }
                .average()
            val premios = data.sumOf { estimation -> estimation.estimatedReward }
            val gasto = data.size * 15 * 0.5 // numDías * 15 tiradas * 0.5 que cuesta la tirada
            val ganancias = premios - gasto
            
            Text(text = "En los últimos ${data.size} días: \nMedia premiados: ${avg}\nTotal premios: ${premios}\nTotal ganancia: $ganancias")
        }
        
        items(data) { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item.date.getFormatedToDate().toUIDate())
                Text(text = "Est. Acertadas(${item.estimationsWithReward.filter { it.isNotEmpty() }.size})")
                Text(text = "Premio: ${item.estimatedReward}€")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    View(
        goBack = {}, data = listOf(
            ReportData(date = getTodayString(), winnerList = listOf(2, 13, 39, 5, 41, 33), estimationsWithReward = listOf(listOf(2)), estimatedReward = 20),
            ReportData(date = getTodayString().getNextDay(1), winnerList = listOf(2, 13, 39, 5, 41, 33), estimationsWithReward = listOf(listOf(2), listOf(2)), estimatedReward = 20),
            ReportData(date = getTodayString().getNextDay(2), winnerList = listOf(2, 13, 39, 5, 41, 33), estimationsWithReward = listOf(listOf()), estimatedReward = 20),
            ReportData(date = getTodayString().getNextDay(3), winnerList = listOf(2, 13, 39, 5, 41, 33), estimationsWithReward = listOf(listOf(2)), estimatedReward = 20),
        )
    )
}