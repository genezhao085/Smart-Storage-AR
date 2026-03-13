package com.smartstoragear.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SpaceEntity::class, SpaceNodeEntity::class, ItemEntity::class, ScanEntity::class, ScanPhotoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun spaceDao(): SpaceDao
    abstract fun spaceNodeDao(): SpaceNodeDao
    abstract fun itemDao(): ItemDao
    abstract fun scanDao(): ScanDao
}
