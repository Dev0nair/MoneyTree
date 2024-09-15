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
        val usualPNs = getTopPns(date, 6) // con 6 va bien
        val importantList = pns
            .sortedByDescending { nd ->
                nd.punctuation.toInt() in usualPNs.map { it.toInt() }
                //nd.punctuation >= -3 && nd.punctuation <= 7.5
            }
        
        return pds.flatMap { res ->
            val numbers = chooseNearestNumber(listPNs = importantList, pn = res.pn, (90 / pds.size + 1))
            numbers.map { num ->
                Statistic(pd = res.pd, pn = res.pn, number = num)
            }
        }
        
    }
    
    fun generateEstimations(statistic: List<Statistic>): List<List<NumberData>> {
        val result = mutableListOf<List<Statistic>>()
        
        (1..15).forEach { sequence ->
            val numbers = mutableListOf<Statistic>()
            val sortedStatistics = statistic.filter { it !in result.flatten() }.sortedBy { (it.pd - it.pn).absoluteValue }
            
            (1..6).forEach { _ ->
                try {
                    val newNumber = sortedStatistics.first { n -> n.number !in numbers.map { it.number } }
                    numbers.add(newNumber)
                } catch (e: Exception) {
                }
            }
            result.add(numbers)
        }
        
        return result.map { res -> res.map { NumberData(it.number, it.pn) } }
    }
    
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