package com.ismaelgr.domain.usecase

import com.ismaelgr.domain.IRepository
import com.ismaelgr.domain.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetResultsUseCase @Inject constructor(
    private val repository: IRepository
) {

    operator fun invoke(date: String): Flow<Result?> = flow {
        val result = repository.getResult(date)
        emit(result)
    }

    operator fun invoke(max: Int): Flow<List<Result>> = flow {
        val results = repository.getResults().sortedByDescending { it.date }.take(max)
        emit(results)
    }
}