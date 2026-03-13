package com.smartstoragear.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SpaceDao {
    @Insert suspend fun insert(space: SpaceEntity): Long
    @Update suspend fun update(space: SpaceEntity)
    @Query("SELECT * FROM spaces ORDER BY updatedAt DESC") fun observeSpaces(): Flow<List<SpaceEntity>>
    @Query("SELECT * FROM spaces WHERE id = :id") fun observeSpace(id: Long): Flow<SpaceEntity?>
}

@Dao
interface SpaceNodeDao {
    @Insert suspend fun insertAll(nodes: List<SpaceNodeEntity>): List<Long>
    @Query("SELECT * FROM space_nodes WHERE spaceId = :spaceId") fun observeBySpace(spaceId: Long): Flow<List<SpaceNodeEntity>>
    @Query("SELECT * FROM space_nodes WHERE id = :id") suspend fun getById(id: Long): SpaceNodeEntity?
}

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(item: ItemEntity): Long
    @Query("SELECT * FROM items WHERE id = :id") fun observeItem(id: Long): Flow<ItemEntity?>
    @Query("SELECT * FROM items ORDER BY updatedAt DESC") fun observeAll(): Flow<List<ItemEntity>>
    @Query("SELECT * FROM items WHERE name LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun search(query: String): Flow<List<ItemEntity>>
}

@Dao
interface ScanDao {
    @Insert suspend fun insert(scan: ScanEntity): Long
    @Insert suspend fun insertPhotos(photos: List<ScanPhotoEntity>)
    @Query("SELECT * FROM scans WHERE spaceId = :spaceId ORDER BY createdAt DESC") fun observeScans(spaceId: Long): Flow<List<ScanEntity>>
}
