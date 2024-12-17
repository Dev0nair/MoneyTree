package com.ismaelgr.domain.usecase

import android.util.Log
import com.ismaelgr.domain.IRepository
import com.ismaelgr.domain.addDays
import com.ismaelgr.domain.getDateFormated
import com.ismaelgr.domain.getDay0
import com.ismaelgr.domain.getFormatedToDate
import com.ismaelgr.domain.getTodayString
import com.ismaelgr.domain.iterateUntilDay
import com.ismaelgr.domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject

/*
* Descarga los resultados de internet y los guarda.
* */
class DownloadDataUseCase @Inject constructor(
    private val iRepository: IRepository
) {
    
    operator fun invoke(from: String, until: String): Flow<Unit> = flow {
        from.iterateUntilDay(until) { date ->
            val dateStr = date.getDateFormated()
            downloadAndSave(dateStr, dateStr)
        }
    }
    
    // From: last saved, until param until
    fun retrieveUntil(until: String): Flow<Unit> = flow {
        val resultList: List<Result> = iRepository.getResults()
        
        if (resultList.isEmpty()) {
            this.emitAll(invoke())
            return@flow
        }
        val lastSaved: String = iRepository.getResults()
            .maxBy { it.date }
            .let { Calendar.getInstance().apply { time = it.date.getFormatedToDate(); addDays(1) } }.time
            .getDateFormated()
        
        lastSaved.iterateUntilDay(until) { date ->
            val dateStr = date.getDateFormated()
            downloadAndSave(dateStr, dateStr)
        }
    }
    
    // Example: from a day of the last week until today
    fun retrieveFrom(from: String): Flow<Unit> = flow {
        from.iterateUntilDay(getTodayString()) { date ->
            val dateStr = date.getDateFormated()
            downloadAndSave(dateStr, dateStr)
        }
    }
    
    // Retrieve everything
    operator fun invoke(): Flow<Unit> = flow {
        val calendar: Calendar = Calendar.getInstance().apply { time = getDay0().getFormatedToDate() }
        val today = Calendar.getInstance()
        
        while (calendar.before(today)) {
            val from = calendar.time.getDateFormated()
            calendar.addDays(100)
            val until = calendar.time.getDateFormated()
            
            downloadAndSave(from, until)
        }
    }
    
    private suspend fun downloadAndSave(from: String, until: String) {
        try {
            val results = iRepository.downloadData(from, until)
            results.forEach { result ->
                iRepository.addResult(result)
            }
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, e.message.orEmpty())
        }
    }
}