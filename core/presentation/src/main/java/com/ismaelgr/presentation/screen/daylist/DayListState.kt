package com.ismaelgr.presentation.screen.daylist

import com.ismaelgr.domain.model.Result

sealed class DayListState {
    data class Empty(val loadingState: String = "") : DayListState()
    data class Data(val data: List<Result>) : DayListState()
}
