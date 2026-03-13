package com.smartstoragear.core.data.repository

data class Space(
    val id: Long,
    val name: String,
    val coverImageUri: String?,
    val createdAt: Long,
    val updatedAt: Long
)

data class SpaceNode(
    val id: Long,
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

data class Item(
    val id: Long,
    val name: String,
    val imageUri: String?,
    val category: String?,
    val note: String?,
    val tags: List<String>,
    val boundSpaceNodeId: Long?,
    val createdAt: Long,
    val updatedAt: Long
)
