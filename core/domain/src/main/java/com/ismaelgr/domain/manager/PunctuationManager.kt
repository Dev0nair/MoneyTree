package com.ismaelgr.domain.manager

import com.ismaelgr.domain.IRepository
import com.ismaelgr.domain.before
import com.ismaelgr.domain.median
import com.ismaelgr.domain.model.DateData
import com.ismaelgr.domain.model.NumberData
import com.ismaelgr.domain.model.PDResult
import com.ismaelgr.domain.roundTo
import javax.inject.Inject

class PunctuationManager @Inject constructor(private val iRepository: IRepository) {

    /**
     * Refactor: useSaved is useless. Should take it out on a new function to recover the current saved DateData of Date
     * Should return a list of 49 items
     * */
    fun generatePnOfDate(date: String, useSaved: Boolean): List<DateData> {
        if (useSaved) {
            val currentDateData: List<DateData> = iRepository.getDateData(date)
            if (currentDateData.isNotEmpty()) return currentDateData
        }
        val dateDataList: MutableList<DateData> = mutableListOf()
        val resultList = iRepository.getResults()
            .filter { result -> result.date.before(date) }
            .sortedBy { it.date }


        (1..49).forEach { number ->
            val appearances: MutableList<Int> = mutableListOf()
            var counter: Int = 0

            resultList.forEach { result ->
                if (result.numberList.contains(number)) {
                    appearances.add(counter)
                    counter = 0
                } else {
                    counter++
                }
            }
            val pn = median(appearances) - counter
            dateDataList.add(
                DateData(
                    date = date,
                    numberData = NumberData(
                        number = number,
                        punctuation = pn.roundTo()
                    )
                )
            )
        }

        return dateDataList
    }

    fun generatePdOfDate(date: String, useSaved: Boolean): List<PDResult> {
        if (useSaved) {
            val currentPDResult: List<PDResult> = iRepository.getPDResult(date)
            if (currentPDResult.isNotEmpty()) return currentPDResult
        }
        // List of winner punctuations. Will only get 6 PNs per day.
        val dateDataList: List<DateData> = iRepository.getDateData()
            .filter { dateData -> dateData.date.before(date) }
            .sortedBy { it.date }
        val totalPns: List<Double> = dateDataList.map { dd -> dd.numberData.punctuation }.distinct()
        val pdList: MutableList<PDResult> = mutableListOf()

        totalPns.forEach { pn ->
            val appearances: MutableList<Int> = mutableListOf()
            var counter: Int = 0

            dateDataList.forEach { dd ->
                if (dd.numberData.punctuation.toInt() == pn.toInt()) {
                    appearances.add(counter)
                    counter = 0
                } else {
                    counter++
                }
            }
            val pd = median(appearances) - counter
            pdList.add(PDResult(date = date, pn = pn, pd = pd.roundTo()))
        }

        return pdList
    }

}