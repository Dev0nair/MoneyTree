package com.ismaelgr.domain.usecase

import com.ismaelgr.domain.IRepository
import com.ismaelgr.domain.manager.PunctuationManager
import com.ismaelgr.domain.manager.SortedByPunctuation
import com.ismaelgr.domain.manager.StatisticManager
import com.ismaelgr.domain.model.NumberData
import com.ismaelgr.domain.model.PDResult
import com.ismaelgr.domain.model.Statistic
import com.ismaelgr.domain.toPrize
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CalculateSetsToWinUseCase @Inject constructor(
    private val iRepository: IRepository,
    private val punctuationManager: PunctuationManager,
    private val statisticManager: StatisticManager
) {
    
    sealed class Data {
        data class Input(val date: String, val profit: Double, val maxTries: Int)
        data class Output(val date: String, val totalWin: Int, val numSetsNeeded: Int)
    }
    
    operator fun invoke(input: Data.Input) = flow<Data.Output> {
        val dateResults = iRepository.getResult(input.date)
        if (!dateResults?.numberList.isNullOrEmpty()) {
            val pnUntilDate: List<NumberData> = punctuationManager.generatePnOfDate(input.date, false).map { dd -> dd.numberData }
            val pdUntilDate: List<PDResult> = punctuationManager.generatePdOfDate(input.date, true)
            val statistics = statisticManager
                .generateStatistics(date = input.date, pds = pdUntilDate, pns = pnUntilDate)
                .toSet()
                .toList()
                .SortedByPunctuation()
            val setsGenerated = mutableListOf<List<Statistic>>()
            
            fun checkPrize(): Int {
                return setsGenerated.sumOf { set ->
                    set.count { st -> st.number in dateResults?.numberList.orEmpty() }.toPrize()
                }
            }
            
            fun isSetEmpty(set: List<Statistic>): Boolean {
                return set.none { it.number > 0 }
            }
            
            while (setsGenerated.size < input.maxTries && checkPrize() < input.profit) {
                val newSet = statisticManager.newGenerateSet(statistics.filter { st -> st !in setsGenerated.flatten() })
                if (newSet !in setsGenerated && !isSetEmpty(newSet)) {
                    setsGenerated.add(newSet)
                }
                
                if (isSetEmpty(newSet)) {
                    break
                }
            }
            
            emit(Data.Output(date = input.date, numSetsNeeded = setsGenerated.size, totalWin = checkPrize()))
        }
    }
}