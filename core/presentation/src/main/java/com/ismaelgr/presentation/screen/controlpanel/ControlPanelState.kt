package com.ismaelgr.presentation.screen.controlpanel

import com.ismaelgr.domain.model.EstimatedResult

sealed interface ControlPanelState {
    data class Empty(val msg: String = "") : ControlPanelState
    data class Data(val data: List<EstimatedResult>) : ControlPanelState
}