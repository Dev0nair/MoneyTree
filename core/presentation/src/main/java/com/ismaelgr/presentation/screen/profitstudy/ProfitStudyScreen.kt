package com.ismaelgr.presentation.screen.profitstudy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.ismaelgr.domain.usecase.CalculateSetsToWinUseCase
import com.ismaelgr.presentation.component.ToolbarComponent
import com.ismaelgr.presentation.screen.loading.LoadingScreen
import com.ismaelgr.presentation.screen.profitstudy.ProfitStudioConfig.MinProfitPerCalc
import com.ismaelgr.presentation.toUIDate

@Composable
fun ProfitStudyScreen(
    goBack: () -> Unit
) {
    val viewModel: ProfitStudioViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    
    if (state is ProfitStudioState.Loading) {
        LoadingScreen(loadingMsg = (state as ProfitStudioState.Loading).msg)
    } else {
        View(goBack, (state as ProfitStudioState.Data).data, MinProfitPerCalc)
    }
    
}

@Composable
private fun View(
    goBack: () -> Unit,
    data: List<CalculateSetsToWinUseCase.Data.Output>,
    minProfit: Double
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            ToolbarComponent(title = "Profit Studio", goBackClick = goBack)
        }
        
        item {
            Column(
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                val minGanado = data.sumOf { it.totalWin }
                val gastado = data.sumOf { it.numSetsNeeded } * 0.5
                
                Text(text = "Resumen: ${data.count { it.totalWin > 0 }} días con premio de ${data.size} días")
                Text(text = "Premio mínimo establecido por día: $minProfit")
                Text(text = "Ganado: $minGanado")
                Text(text = "Gasto en sets: $gastado")
                Text(text = "Beneficio: ${minGanado - gastado}")
            }
        }
        val filteredData = if (ProfitStudioConfig.FilterOnlyWinner) {
            data.filter { it.totalWin > (it.numSetsNeeded * 0.5) }.sortedByDescending { it.date }
        } else {
            data
        }
        
        items(filteredData) { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Text(item.date.getFormatedToDate().toUIDate())
                Text(text = "Sets: ${item.numSetsNeeded}")
                Text(text = "Ganado: ${item.totalWin}")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    View(
        goBack = { }, listOf(
            CalculateSetsToWinUseCase.Data.Output(getTodayString(), 12, 12),
            CalculateSetsToWinUseCase.Data.Output(getTodayString().getNextDay(1), 21, 21),
            CalculateSetsToWinUseCase.Data.Output(getTodayString().getNextDay(2), 33, 21),
        ),
        minProfit = 20.0
    )
}