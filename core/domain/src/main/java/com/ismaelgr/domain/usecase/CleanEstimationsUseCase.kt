package com.ismaelgr.domain.usecase

import com.ismaelgr.domain.IRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CleanEstimationsUseCase @Inject constructor(private val iRepository: IRepository) {

    operator fun invoke() = flow<Unit> {
        iRepository.cleanDateData()
        iRepository.cleanPDResults()
    }
}