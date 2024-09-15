package com.ismaelgr.presentation.model

data class ReportData(
    val date: String,
    val winnerList: List<Int>,
    val estimationsWithReward: List<List<Int>>,
    val estimatedReward: Int
)
