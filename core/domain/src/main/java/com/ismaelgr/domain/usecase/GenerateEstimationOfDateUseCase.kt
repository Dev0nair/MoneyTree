package com.ismaelgr.domain.usecase

import android.util.Log
import com.ismaelgr.domain.IRepository
import com.ismaelgr.domain.manager.PunctuationManager
import com.ismaelgr.domain.manager.StatisticManager
import com.ismaelgr.domain.model.EstimatedResult
import com.ismaelgr.domain.model.NumberData
import com.ismaelgr.domain.model.PDResult
import com.ismaelgr.domain.model.Summary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GenerateEstimationOfDateUseCase @Inject constructor(
    private val iRepository: IRepository,
    private val punctuationManager: PunctuationManager,
    private val statisticManager: StatisticManager
) {
    
    operator fun invoke(date: String): Flow<EstimatedResult> = flow {
        Log.i("ESTIMATION", "Comienza estimación para $date")
        val dateResults = iRepository.getResult(date)
        val pnUntilDate: List<NumberData> = punctuationManager.generatePnOfDate(date, false).map { dd -> dd.numberData }
        val pdUntilDate: List<PDResult> = punctuationManager.generatePdOfDate(date, false)
        val statistics = statisticManager.generateStatistics(date = date, pds = pdUntilDate, pns = pnUntilDate).toSet().toList()
        val estimation: List<List<NumberData>> =
            statisticManager.generateEstimations(statistics, date = date)
        val winnerAnalysis: List<Summary> = dateResults?.numberList?.mapNotNull { winner ->
            pnUntilDate.firstOrNull { s -> s.number == winner }
        }?.map { d ->
            Summary(
                number = d.number,
                pn = d.punctuation,
                pd = statisticManager.chooseNearestPD(listPDs = pdUntilDate, d.punctuation, 1).firstOrNull()
            )
        }
            ?: emptyList()
        val result = EstimatedResult(
            date = date,
            statistic = statistics,
            estimations = estimation,
            winnerList = dateResults?.numberList
                ?: listOf(0, 0, 0, 0, 0, 0),
            winnerListAnalysis = winnerAnalysis
        )
        Log.i("ESTIMATION", "Finaliza estimación para $date")
        
        emit(result)
    }
    
}