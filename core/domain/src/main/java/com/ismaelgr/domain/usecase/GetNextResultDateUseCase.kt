package com.ismaelgr.domain.usecase

import com.ismaelgr.domain.IRepository
import com.ismaelgr.domain.addDays
import com.ismaelgr.domain.getDateFormated
import com.ismaelgr.domain.getFormatedToDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject

class GetNextResultDateUseCase @Inject constructor(private val iRepository: IRepository) {
    
    operator fun invoke(): Flow<String> = flow {
        val lastResult = iRepository.getResults().maxBy { it.date }.date.getFormatedToDate()
        val nextResult = lastResult.let { date -> Calendar.getInstance().apply { time = date; addDays(1) } }.time
        
        emit(nextResult.getDateFormated())
    }
}