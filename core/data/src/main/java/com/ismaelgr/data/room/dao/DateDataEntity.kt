package com.ismaelgr.data.room.dao

import androidx.room.Entity

@Entity(tableName = "DateData", primaryKeys = ["date", "number"])
data class DateDataEntity(
    val date: String,
    val number: Int,
    val punctuation: Double // Puntuación del día anterior, para el estudio sin el resultado de date
)