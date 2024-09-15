package com.ismaelgr.domain.usecase

import com.ismaelgr.domain.IRepository
import com.ismaelgr.domain.model.EstimatedResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetEstimatedResultOfUseCase @Inject constructor(private val iRepository: IRepository) {
    
    operator fun invoke(date: String): Flow<EstimatedResult> = flow {
    }
}