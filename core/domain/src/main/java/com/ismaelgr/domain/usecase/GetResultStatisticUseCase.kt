package com.ismaelgr.domain.usecase

import com.ismaelgr.domain.IRepository
import com.ismaelgr.domain.getFormatedToDate
import com.ismaelgr.domain.model.DateData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetResultStatisticUseCase @Inject constructor(private val iRepository: IRepository) {
    
    operator fun invoke(until: String): Flow<List<Pair<String, List<Double>>>> = flow {
        val data: List<DateData> = iRepository.getDateData().filter { it.date.getFormatedToDate().before(until.getFormatedToDate()) }.sortedBy { it.date }
        val results: List<Pair<String, List<Double>>> = data
            .groupBy { d -> d.date }
            .map { d -> d.key to d.value.map { dateData -> dateData.numberData.punctuation } }
        
        emit(results)
    }
}