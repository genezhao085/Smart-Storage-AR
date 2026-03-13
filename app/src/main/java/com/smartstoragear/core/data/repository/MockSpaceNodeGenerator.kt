package com.smartstoragear.core.data.repository

import com.smartstoragear.core.data.db.SpaceNodeEntity
import javax.inject.Inject

class MockSpaceNodeGenerator @Inject constructor() {
    fun generate(spaceId: Long): List<SpaceNodeEntity> = listOf(
        SpaceNodeEntity(spaceId = spaceId, parentId = null, type = "room", name = "Main Room", centerX = 0f, centerY = 0f, centerZ = -1f, sizeX = 4f, sizeY = 2.5f, sizeZ = 4f, confidence = 0.92f),
        SpaceNodeEntity(spaceId = spaceId, parentId = null, type = "cabinet", name = "Cabinet A", centerX = -1f, centerY = 0f, centerZ = -2f, sizeX = 1.2f, sizeY = 2f, sizeZ = 0.5f, confidence = 0.86f),
        SpaceNodeEntity(spaceId = spaceId, parentId = null, type = "shelf", name = "Shelf Top", centerX = 1f, centerY = 1.2f, centerZ = -1.5f, sizeX = 1f, sizeY = 0.3f, sizeZ = 0.4f, confidence = 0.82f)
    )
}
