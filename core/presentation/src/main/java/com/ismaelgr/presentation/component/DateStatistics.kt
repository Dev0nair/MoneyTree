package com.ismaelgr.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ismaelgr.domain.model.EstimatedResult

@Composable
fun DateStatistics(estimatedResult: EstimatedResult) {
    Column(
        modifier = Modifier.padding(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Summary(estimatedResult = estimatedResult)
        EstimationList(estimatedResult = estimatedResult)
        WinnerListAnalysis(estimatedResult = estimatedResult)
        Statistics(estimatedResult = estimatedResult)
    }
}

@Composable
private fun Summary(estimatedResult: EstimatedResult) {
    val estimationsWithRewards = estimatedResult.estimations
        .map { es -> es.count { nd -> nd.number in estimatedResult.winnerList } }
        .filter { count -> count > 2 }
    val prices: Map<Int, Int> = mapOf(
        3 to 4,
        4 to 25,
        5 to 1000,
        6 to 100000
    )
    val estimatedReward = estimationsWithRewards
        .mapNotNull { count -> prices[count] }
        .reduceOrNull { price, count -> price + count }
        ?: 0
    
    Text(text = "Estimaciones con premios: ${estimationsWithRewards.size}")
    Text(text = "Premio estimado: $estimatedReward€")
}

@Composable
private fun WinnerListAnalysis(estimatedResult: EstimatedResult) {
    if (estimatedResult.winnerListAnalysis.isNotEmpty()) {
        Text("Analisis número ganadores")
        
        estimatedResult.winnerListAnalysis.map { winnerNumber ->
            Text(
                modifier = Modifier.padding(horizontal = 15.dp),
                text = "Number: ${winnerNumber.number}. Pn: ${winnerNumber.pn}. Pd: ${winnerNumber.pd}"
            )
        }
    }
}

@Composable
private fun EstimationList(estimatedResult: EstimatedResult) {
    Column(
        modifier = Modifier.padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        estimatedResult.estimations.mapIndexed { index, estimation ->
            RowResult(estimation = estimation.map { it.number }, winnerList = estimatedResult.winnerList, index = index + 1)
        }
    }
}

@Composable
private fun Statistics(estimatedResult: EstimatedResult) {
    if (estimatedResult.winnerList.find { it != 0 } != null) {
        Text("Estadística")
        val text = estimatedResult.winnerList
            .map { winnerNumber -> estimatedResult.statistic.filter { it.number == winnerNumber } }
            .flatten()
            .joinToString(separator = "\n") { "${it.number} -> PD(${it.pd}). PN(${it.pn})" }
        Text(text = text)
    }
}

@Composable
private fun RowResult(modifier: Modifier = Modifier, estimation: List<Int>, winnerList: List<Int>, index: Int) {
    val borderColor = if (estimation.filter { es -> es in winnerList }.size > 2) Color.Green.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primaryContainer
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = borderColor, shape = RoundedCornerShape(5.dp))
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ResultNumbersComponent(modifier = Modifier.weight(1f), numbers = estimation, date = index.toString(), winners = winnerList, showDate = false)
    }
}