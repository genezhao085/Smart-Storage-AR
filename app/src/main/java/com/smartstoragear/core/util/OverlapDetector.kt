package com.smartstoragear.core.util

import android.graphics.Bitmap
import kotlin.math.abs
import javax.inject.Inject

class OverlapDetector @Inject constructor() {
    fun angleBucket(index: Int, maxBuckets: Int = 8): Int = index % maxBuckets

    fun isDuplicateAngle(newAngleBucket: Int, existingBuckets: List<Int>): Boolean {
        return existingBuckets.any { abs(it - newAngleBucket) <= 0 }
    }

    fun roughVisualDistanceScore(bitmap: Bitmap): Double {
        val stepX = (bitmap.width / 20).coerceAtLeast(1)
        val stepY = (bitmap.height / 20).coerceAtLeast(1)
        var score = 0L
        var count = 0
        for (x in 0 until bitmap.width step stepX) {
            for (y in 0 until bitmap.height step stepY) {
                score += bitmap.getPixel(x, y).toLong() and 0xFF
                count++
            }
        }
        return score.toDouble() / count.toDouble()
    }
}
