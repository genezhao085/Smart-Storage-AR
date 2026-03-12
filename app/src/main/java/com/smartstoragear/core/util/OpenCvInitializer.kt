package com.smartstoragear.core.util

import javax.inject.Inject
import org.opencv.android.OpenCVLoader

class OpenCvInitializer @Inject constructor() {
    fun init(): Boolean = OpenCVLoader.initLocal()
}
