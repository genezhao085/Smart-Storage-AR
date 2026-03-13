package com.smartstoragear.core.data.repository

import com.smartstoragear.core.data.db.ItemDao
import com.smartstoragear.core.data.db.ItemEntity
import com.smartstoragear.core.data.db.ScanDao
import com.smartstoragear.core.data.db.ScanEntity
import com.smartstoragear.core.data.db.ScanPhotoEntity
import com.smartstoragear.core.data.db.SpaceDao
import com.smartstoragear.core.data.db.SpaceEntity
import com.smartstoragear.core.data.db.SpaceNodeDao
import com.smartstoragear.core.data.db.SpaceNodeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface SpaceRepository {
    fun observeSpaces(): Flow<List<Space>>
    fun observeSpace(spaceId: Long): Flow<Space?>
    fun observeNodes(spaceId: Long): Flow<List<SpaceNode>>
    suspend fun createSpace(name: String, coverImageUri: String?): Long
    suspend fun saveScan(spaceId: Long, photos: List<CapturedPhoto>)
    suspend fun generateMockNodes(spaceId: Long)
}

data class CapturedPhoto(val uri: String, val angleBucket: Int, val varianceScore: Double)

@Singleton
class SpaceRepositoryImpl @Inject constructor(
    private val spaceDao: SpaceDao,
    private val scanDao: ScanDao,
    private val nodeDao: SpaceNodeDao,
    private val mockSpaceNodeGenerator: MockSpaceNodeGenerator
) : SpaceRepository {
    override fun observeSpaces(): Flow<List<Space>> = spaceDao.observeSpaces().map { list -> list.map { it.toDomain() } }
    override fun observeSpace(spaceId: Long): Flow<Space?> = spaceDao.observeSpace(spaceId).map { it?.toDomain() }
    override fun observeNodes(spaceId: Long): Flow<List<SpaceNode>> = nodeDao.observeBySpace(spaceId).map { it.map { n -> n.toDomain() } }

    override suspend fun createSpace(name: String, coverImageUri: String?): Long {
        val now = System.currentTimeMillis()
        return spaceDao.insert(SpaceEntity(name = name, coverImageUri = coverImageUri, createdAt = now, updatedAt = now))
    }

    override suspend fun saveScan(spaceId: Long, photos: List<CapturedPhoto>) {
        val scanId = scanDao.insert(ScanEntity(spaceId = spaceId, status = "completed", createdAt = System.currentTimeMillis()))
        scanDao.insertPhotos(photos.map { ScanPhotoEntity(scanId = scanId, uri = it.uri, angleBucket = it.angleBucket, varianceScore = it.varianceScore) })
    }

    override suspend fun generateMockNodes(spaceId: Long) {
        nodeDao.insertAll(mockSpaceNodeGenerator.generate(spaceId))
    }
}

interface ItemRepository {
    fun observeAll(): Flow<List<Item>>
    fun observeItem(itemId: Long): Flow<Item?>
    fun search(query: String): Flow<List<Item>>
    suspend fun createItem(name: String, category: String, note: String, tags: List<String>, nodeId: Long?)
}

@Singleton
class ItemRepositoryImpl @Inject constructor(private val itemDao: ItemDao) : ItemRepository {
    override fun observeAll(): Flow<List<Item>> = itemDao.observeAll().map { it.map { e -> e.toDomain() } }
    override fun observeItem(itemId: Long): Flow<Item?> = itemDao.observeItem(itemId).map { it?.toDomain() }
    override fun search(query: String): Flow<List<Item>> = itemDao.search(query).map { it.map { i -> i.toDomain() } }

    override suspend fun createItem(name: String, category: String, note: String, tags: List<String>, nodeId: Long?) {
        val now = System.currentTimeMillis()
        itemDao.insert(
            ItemEntity(
                name = name,
                imageUri = null,
                category = category,
                note = note,
                tags = tags.joinToString(","),
                boundSpaceNodeId = nodeId,
                createdAt = now,
                updatedAt = now
            )
        )
    }
}

private fun SpaceEntity.toDomain() = Space(id, name, coverImageUri, createdAt, updatedAt)
private fun SpaceNodeEntity.toDomain() = SpaceNode(id, spaceId, parentId, type, name, centerX, centerY, centerZ, sizeX, sizeY, sizeZ, confidence)
private fun ItemEntity.toDomain() = Item(id, name, imageUri, category, note, tags.split(",").filter { it.isNotBlank() }, boundSpaceNodeId, createdAt, updatedAt)
