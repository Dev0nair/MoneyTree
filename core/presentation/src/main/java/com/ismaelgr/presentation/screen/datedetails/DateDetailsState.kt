package com.ismaelgr.presentation.screen.datedetails

import com.ismaelgr.domain.model.EstimatedResult

sealed interface DateDetailsState {
    data class Empty(val loadingMsg: String = "") : DateDetailsState
    data class Data(val estimatedResult: EstimatedResult) : DateDetailsState
    data class Error(val msg: String) : DateDetailsState
}