package com.ismaelgr.data.repository

import com.ismaelgr.data.retrofit.RetrofitDataSource
import com.ismaelgr.data.retrofit.dao.Bonoloto
import com.ismaelgr.data.room.RoomDataSource
import com.ismaelgr.data.toData
import com.ismaelgr.data.toDomain
import com.ismaelgr.domain.IRepository
import com.ismaelgr.domain.model.DateData
import com.ismaelgr.domain.model.PDResult
import com.ismaelgr.domain.model.Result
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource,
    private val retrofitDataSource: RetrofitDataSource
) : IRepository {
    
    override suspend fun downloadData(fromDate: String, untilDate: String): List<Result> {
        val result: List<Bonoloto> = retrofitDataSource.getBonolotos(fechaInicioInclusiva = fromDate, fechaFinInclusiva = untilDate).body()
            ?: emptyList()
        val parsedData: List<Result> = result.map { bonoloto ->
            val simpleBonoloto = bonoloto.getSimpleBonoloto()
            Result(date = simpleBonoloto.date, numberList = simpleBonoloto.getListNumbers())
        }
        
        return parsedData
    }
    
    override fun getResults(): List<Result> {
        return roomDataSource.getResults().map { it.toDomain() }
    }
    
    override fun getResult(date: String): Result? {
        return roomDataSource.getResult(date)?.toDomain()
    }
    
    override fun addResult(result: Result) {
        roomDataSource.addResult(result.toData())
    }
    
    override fun cleanResults() {
        val results = roomDataSource.getResults()
        results.forEach { result ->
            roomDataSource.removeResult(result)
        }
    }
    
    override fun getDateData(): List<DateData> {
        return roomDataSource.getDateData().map { it.toDomain() }
    }
    
    override fun addDateData(dateData: DateData) {
        roomDataSource.addDateData(dateData.toData())
    }
    
    override fun getDateData(date: String): List<DateData> {
        return roomDataSource.getDateData(date).map { it.toDomain() }
    }
    
    override fun getDateData(date: String, number: Int): DateData? {
        return roomDataSource.getSpecificDateData(date, number)?.toDomain()
    }
    
    override fun cleanDateData() {
        val dateDataList = roomDataSource.getDateData()
        dateDataList.forEach { dateDataEntity ->
            roomDataSource.deleteDateDate(dateDataEntity)
        }
    }
    
    override fun cleanDateData(date: String) {
        val dateDataList = roomDataSource.getDateData(date)
        dateDataList.forEach { dateDataEntity ->
            roomDataSource.deleteDateDate(dateDataEntity)
        }
    }
    
    override fun cleanDateData(date: String, number: Int) {
        roomDataSource.getSpecificDateData(date, number)?.let { dateDataEntity ->
            roomDataSource.deleteDateDate(dateDataEntity)
        }
    }
    
    override fun addPDResult(pdResult: PDResult) {
        roomDataSource.addPDResult(pdResult.toData())
    }
    
    override fun getPDResult(date: String): List<PDResult> {
        return roomDataSource.getPDResult(date).map { it.toDomain() }
    }
    
    override fun getPDResults(): List<PDResult> {
        return roomDataSource.getPDResults().map { it.toDomain() }
    }
    
    override fun cleanPDResults() {
        getPDResults().forEach { pdResult ->
            roomDataSource.cleanPDResults(pdResult.toData())
        }
    }
}