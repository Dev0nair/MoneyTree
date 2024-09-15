package com.ismaelgr.domain.usecase

import com.ismaelgr.domain.IRepository
import com.ismaelgr.domain.model.DateData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStatisticOfDateUseCase @Inject constructor(private val iRepository: IRepository) {
    
    operator fun invoke(date: String): Flow<List<DateData>> = flow {
        val content = iRepository.getDateData(date)
        emit(content)
    }
}