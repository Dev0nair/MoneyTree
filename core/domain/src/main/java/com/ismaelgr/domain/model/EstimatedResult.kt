package com.ismaelgr.domain.model

data class Statistic(
    val pd: Double,
    val pn: Double,
    val number: Int
)

data class EstimatedResult(
    val date: String,
    val winnerList: List<Int>,
    val estimations: List<List<NumberData>>,
    val statistic: List<Statistic>,
    val winnerListAnalysis: List<Summary> = emptyList()
)
