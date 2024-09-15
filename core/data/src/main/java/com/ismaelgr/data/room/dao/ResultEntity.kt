package com.ismaelgr.data.room.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Result")
data class ResultEntity(
    @PrimaryKey
    val date: String,
    val numberList: String
)
