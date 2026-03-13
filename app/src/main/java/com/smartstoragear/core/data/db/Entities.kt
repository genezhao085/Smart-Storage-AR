package com.smartstoragear.core.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "spaces")
data class SpaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val coverImageUri: String?,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(
    tableName = "space_nodes",
    foreignKeys = [
        ForeignKey(entity = SpaceEntity::class, parentColumns = ["id"], childColumns = ["spaceId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("spaceId")]
)
data class SpaceNodeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val spaceId: Long,
    val parentId: Long?,
    val type: String,
    val name: String,
    val centerX: Float,
    val centerY: Float,
    val centerZ: Float,
    val sizeX: Float,
    val sizeY: Float,
    val sizeZ: Float,
    val confidence: Float
)

@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(entity = SpaceNodeEntity::class, parentColumns = ["id"], childColumns = ["boundSpaceNodeId"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [Index("boundSpaceNodeId")]
)
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val imageUri: String?,
    val category: String?,
    val note: String?,
    val tags: String,
    val boundSpaceNodeId: Long?,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(
    tableName = "scans",
    foreignKeys = [
        ForeignKey(entity = SpaceEntity::class, parentColumns = ["id"], childColumns = ["spaceId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("spaceId")]
)
data class ScanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val spaceId: Long,
    val status: String,
    val createdAt: Long
)

@Entity(
    tableName = "scan_photos",
    foreignKeys = [
        ForeignKey(entity = ScanEntity::class, parentColumns = ["id"], childColumns = ["scanId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("scanId")]
)
data class ScanPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val scanId: Long,
    val uri: String,
    val angleBucket: Int,
    val varianceScore: Double
)
