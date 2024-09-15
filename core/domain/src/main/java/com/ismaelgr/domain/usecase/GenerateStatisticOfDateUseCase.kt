package com.ismaelgr.domain.usecase

import com.ismaelgr.domain.IRepository
import com.ismaelgr.domain.getNextDay
import com.ismaelgr.domain.getTodayString
import com.ismaelgr.domain.manager.PunctuationManager
import com.ismaelgr.domain.model.DateData
import com.ismaelgr.domain.model.PDResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class GenerateStatisticOfDateUseCase @Inject constructor(
    private val iRepository: IRepository,
    private val punctuationManager: PunctuationManager
) {
    
    /*
    * 1. Se cogen todos los resultados en orden de menor a mayor (por fecha) y se itera
    * 2. Por cada iteración, se recoge la estadística de los números del 1 al 49 desde el primer resultado hasta el actual iterado
    * Luego, se recoge la estadística
    * */
    operator fun invoke(numDays: Int): Flow<Unit> = flow {
        ((-1 * numDays)..0).forEach { i ->
            val currentDate: String = getTodayString().getNextDay(i)
            iRepository.getResult(currentDate)?.numberList?.let { winnerList ->
                // De aquí solo cogemos los PN Ganadores
                val pnList: List<DateData> = punctuationManager.generatePnOfDate(currentDate, true).filter { dd -> dd.numberData.number in winnerList }
                pnList.forEach(iRepository::addDateData)
                // De aquí solo cogemos los PD ganadores
                val pdList: List<PDResult> = punctuationManager.generatePdOfDate(currentDate, true).filter { pd -> pd.pn in pnList.map { pn -> pn.numberData.punctuation } }
                pdList.forEach(iRepository::addPDResult)
            }
        }
    }
    
    fun backgroundLoading(numDays: Int): Flow<Unit> = flow {
        ((-1 * numDays)..0).forEach { i ->
            val currentDate: String = getTodayString().getNextDay(i)
            
            GlobalScope.launch(Dispatchers.Default) {
                iRepository.getResult(currentDate)?.numberList?.let { winnerList ->
                    val pnList: List<DateData> = punctuationManager.generatePnOfDate(currentDate, true).filter { dd -> dd.numberData.number in winnerList }
                    pnList.forEach(iRepository::addDateData)
                    val pdList: List<PDResult> = punctuationManager.generatePdOfDate(currentDate, true).filter { pd -> pd.pn in pnList.map { pn -> pn.numberData.punctuation } }
                    pdList.forEach(iRepository::addPDResult)
                }
            }
        }
    }
    
}