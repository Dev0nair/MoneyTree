package com.ismaelgr.presentation.screen.profitstudy

import com.ismaelgr.domain.usecase.CalculateSetsToWinUseCase

sealed class ProfitStudioState {
    data class Loading(val msg: String) : ProfitStudioState()
    data class Data(val data: List<CalculateSetsToWinUseCase.Data.Output>) : ProfitStudioState()
}