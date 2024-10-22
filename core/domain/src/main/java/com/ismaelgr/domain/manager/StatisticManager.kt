package com.ismaelgr.domain.manager

import com.ismaelgr.domain.IRepository
import com.ismaelgr.domain.before
import com.ismaelgr.domain.model.NumberData
import com.ismaelgr.domain.model.PDResult
import com.ismaelgr.domain.model.Statistic
import javax.inject.Inject
import kotlin.math.absoluteValue

class StatisticManager @Inject constructor(private val iRepository: IRepository) {
    
    fun generateStatistics(date: String, pns: List<NumberData>, pds: List<PDResult>): List<Statistic> {
        //        val usualPNs: List<Double> = getTopPns(date, 6) // con 6 va bien
        //        val sortedPnList: List<NumberData> = pns
        //            .sortedWith(
        //                compareBy(
        //                    { nd -> nd.punctuation.toInt() !in usualPNs.map { it.toInt() } }, // Con el !in, los que aparezcan serán false e irán al principio
        //                    { it.punctuation }, // Además, serán ordenados por puntuación de menos a mayor
        //                )
        //            )
        val numStatisticsToGenerate = 15 * 6 // Number of sets * numbers per set
        
        return pds.flatMap { res ->
            // this calc takes the correct amount to get all the numbers needed for the sets -> numStatisticsToGenerate / pds.size + 1. Per PD, we get the nearest x pns with the nearest number associated
            val numbers = chooseNearestNumber(
                listPNs = pns,
                pn = res.pn,
                (numStatisticsToGenerate / pds.size + 1)
            )
            numbers.map { num ->
                Statistic(pd = res.pd, pn = res.pn, number = num)
            }
        }
    }
    
    fun generateEstimations(statistic: List<Statistic>, quantity: Int = 15): List<List<NumberData>> {
        val result = mutableListOf<List<Statistic>>()
        
        fun isSetEmpty(set: List<Statistic>): Boolean {
            return set.none { it.number > 0 }
        }

        val sortedStatistic = statistic.SortedByPunctuation()
        
        while (result.size < quantity) {
            val newResult = newGenerateSet(sortedStatistic.filter { st -> st !in result.flatten() })
            if (newResult !in result && !isSetEmpty(newResult)) {
                result.add(newResult)
            }
            
            if (isSetEmpty(newResult)) {
                break
            }
        }
        
        return result.map { res ->
            res.map { static -> NumberData(static.number, static.pn) }
        }
    }
    
    fun newGenerateSet(
        statistic: List<Statistic>,
    ): List<Statistic> = statistic
        .distinctBy { it.number }
        .take(6)
    
    @Suppress("SameParameterValue")
    private fun getTopPns(date: String, topCount: Int): List<Double> {
        return iRepository.getDateData()
            .filter { dd -> dd.date.before(date) }
            .groupBy { dd -> dd.numberData.punctuation }
            .mapValues { it.value.count() }
            .toList()
            .sortedByDescending { pair -> pair.second }
            .map { pair -> pair.first }
            .take(topCount)
    }
    
    private fun chooseNearestNumber(listPNs: List<NumberData>, pn: Double, n: Int): List<Int> {
        return listPNs.map { it.number to (it.punctuation - pn).absoluteValue }.sortedBy { it.second }.map { it.first }.take(n)
    }
    
    fun chooseNearestPD(listPDs: List<PDResult>, pn: Double, n: Int): List<Double> {
        return listPDs.sortedBy { (it.pn - pn).absoluteValue }.map { it.pd }.take(n)
    }
}