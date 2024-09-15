package com.ismaelgr.presentation.model

import com.ismaelgr.domain.model.PDResult

data class DatePD(
    val date: String,
    val pdList: List<PDResult>
)
