package com.ismaelgr.domain

import com.ismaelgr.domain.model.DateData
import com.ismaelgr.domain.model.PDResult
import com.ismaelgr.domain.model.Result

interface IRepository {
    
    suspend fun downloadData(fromDate: String, untilDate: String): List<Result>
    
    fun getResults(): List<Result>
    fun getResult(date: String): Result?
    fun addResult(result: Result)
    fun cleanResults()
    
    fun addDateData(dateData: DateData)
    fun getDateData(): List<DateData>
    fun getDateData(date: String): List<DateData>
    fun getDateData(date: String, number: Int): DateData?
    fun cleanDateData()
    fun cleanDateData(date: String)
    fun cleanDateData(date: String, number: Int)
    
    fun addPDResult(pdResult: PDResult)
    fun getPDResult(date: String): List<PDResult>
    fun getPDResults(): List<PDResult>
    fun cleanPDResults()
}