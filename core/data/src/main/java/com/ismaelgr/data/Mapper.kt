package com.ismaelgr.data

import com.ismaelgr.data.room.dao.DateDataEntity
import com.ismaelgr.data.room.dao.PDResultEntity
import com.ismaelgr.data.room.dao.ResultEntity
import com.ismaelgr.domain.model.DateData
import com.ismaelgr.domain.model.NumberData
import com.ismaelgr.domain.model.PDResult
import com.ismaelgr.domain.model.Result

fun DateDataEntity.toDomain(): DateData = DateData(date, NumberData(number, punctuation))
fun DateData.toData(): DateDataEntity =
    DateDataEntity(date, numberData.number, numberData.punctuation)

fun ResultEntity.toDomain(): Result = Result(date, numberList.toIntList())
fun Result.toData(): ResultEntity = ResultEntity(date, numberList.toNumberList())

fun PDResult.toData(): PDResultEntity = PDResultEntity(date, pn, pd)
fun PDResultEntity.toDomain(): PDResult = PDResult(date, pn, pd)