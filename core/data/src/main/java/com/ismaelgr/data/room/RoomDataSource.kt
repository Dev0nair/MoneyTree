package com.ismaelgr.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ismaelgr.data.room.dao.DateDataEntity
import com.ismaelgr.data.room.dao.PDResultEntity
import com.ismaelgr.data.room.dao.ResultEntity

@Dao
interface RoomDataSource {
    
    @Query("select * from Result")
    fun getResults(): List<ResultEntity>
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addResult(result: ResultEntity)
    
    @Query("select * from Result where date = :date")
    fun getResult(date: String): ResultEntity?
    
    @Delete
    fun removeResult(result: ResultEntity)
    
    @Query("select * from DateData")
    fun getDateData(): List<DateDataEntity>
    
    @Query("select * from DateData where date = :date")
    fun getDateData(date: String): List<DateDataEntity>
    
    @Query("select * from DateData where date = :date and number = :number")
    fun getSpecificDateData(date: String, number: Int): DateDataEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDateData(dateDataEntity: DateDataEntity)
    
    @Delete
    fun deleteDateDate(dateDataEntity: DateDataEntity)
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPDResult(pdResultEntity: PDResultEntity)
    
    @Query("select * from PDresult where date == :date")
    fun getPDResult(date: String): List<PDResultEntity>
    
    @Query("select * from PDresult")
    fun getPDResults(): List<PDResultEntity>
    
    @Delete
    fun cleanPDResults(pdResultEntity: PDResultEntity)
}