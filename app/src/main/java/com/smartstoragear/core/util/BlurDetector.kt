package com.smartstoragear.core.util

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import javax.inject.Inject

class BlurDetector @Inject constructor() {
    fun varianceOfLaplacian(bitmap: Bitmap): Double {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC4)
        Utils.bitmapToMat(bitmap, src)
        val gray = Mat()
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_RGBA2GRAY)
        val laplacian = Mat()
        Imgproc.Laplacian(gray, laplacian, CvType.CV_64F)
        val mean = org.opencv.core.Core.mean(laplacian).`val`[0]
        val sqr = Mat()
        org.opencv.core.Core.multiply(laplacian, laplacian, sqr)
        val meanSqr = org.opencv.core.Core.mean(sqr).`val`[0]
        return meanSqr - (mean * mean)
    }

    fun isBlurry(bitmap: Bitmap, threshold: Double = 80.0): Boolean = varianceOfLaplacian(bitmap) < threshold
}
