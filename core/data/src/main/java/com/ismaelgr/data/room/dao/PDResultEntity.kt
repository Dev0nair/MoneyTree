package com.ismaelgr.data.room.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PDResult")
data class PDResultEntity(
    @PrimaryKey
    val date: String,
    val pn: Double,
    val pd: Double
)