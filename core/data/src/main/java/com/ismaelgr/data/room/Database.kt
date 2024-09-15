package com.ismaelgr.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ismaelgr.data.room.dao.DateDataEntity
import com.ismaelgr.data.room.dao.PDResultEntity
import com.ismaelgr.data.room.dao.ResultEntity

@Database(entities = [ResultEntity::class, DateDataEntity::class, PDResultEntity::class], version = 2)
abstract class Database : RoomDatabase() {
    
    abstract fun dataSource(): RoomDataSource
}