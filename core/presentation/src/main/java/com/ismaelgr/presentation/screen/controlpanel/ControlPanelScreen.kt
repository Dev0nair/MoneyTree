package com.ismaelgr.presentation.screen.controlpanel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ismaelgr.domain.model.EstimatedResult
import com.ismaelgr.domain.model.Summary
import com.ismaelgr.domain.roundTo
import com.ismaelgr.presentation.component.ToolbarComponent
import com.ismaelgr.presentation.screen.loading.LoadingScreen

@Composable
fun ControlPanelScreen() {
    val viewModel: ControlPanelViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val restimationState by viewModel.restimationState.collectAsState()
    
    LaunchedEffect(key1 = Unit) {
        viewModel.loadData()
    }
    
    if (state is ControlPanelViewModel.State.Empty || restimationState is ControlPanelViewModel.RestimationState.Empty) {
        LoadingScreen(loadingMsg = "Cargando datos")
    } else {
        View(
            (state as ControlPanelViewModel.State.Data).data,
            viewModel::navigateUp,
            viewModel::restartEstimations
        )
    }
}

@Composable
fun View(
    data: List<EstimatedResult>,
    goBack: () -> Unit,
    clickRestartEstimations: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToolbarComponent(title = "Control Panel", goBackClick = goBack)
        val list: List<List<Summary>> = data.map { d -> d.winnerListAnalysis.map { Summary(pn = it.pn.roundTo(), pd = it.pd?.roundTo(), number = it.number) } }
        val resultadoPns = list.flatten().groupBy { s -> s.pn }.mapValues { it.value.count() }.toList().sortedByDescending { it.second }.take(6)
        val resultadoPds = list.flatten().groupBy { s -> s.pd }.mapValues { it.value.count() }.toList().sortedByDescending { it.second }.take(15)
        
        Text("PNs")
        resultadoPns.map {
            Text(text = "${it.first} ---> Count(${it.second})")
        }
        
        Text("PDs")
        resultadoPds.map {
            Text(text = "${it.first} ---> Count(${it.second})")
        }
        
        OptionList(
            clickRestartEstimations = clickRestartEstimations
        )
    }
}

@Composable
private fun OptionList(
    clickRestartEstimations: () -> Unit
) {
    val options: List<Pair<String, () -> Unit>> = listOf(
        "Rehacer estudio" to clickRestartEstimations
    )
    
    LazyVerticalGrid(modifier = Modifier.fillMaxWidth(), columns = GridCells.Fixed(count = 2)) {
        items(options) { option ->
            Button(onClick = option.second) {
                Text(option.first)
            }
        }
    }
    
}