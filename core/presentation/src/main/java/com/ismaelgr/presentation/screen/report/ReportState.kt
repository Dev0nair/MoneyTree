package com.ismaelgr.presentation.screen.report

import com.ismaelgr.presentation.model.ReportData

sealed interface ReportState {
    data class Empty(val msg: String = "") : ReportState
    data class Data(val data: List<ReportData>) : ReportState
}