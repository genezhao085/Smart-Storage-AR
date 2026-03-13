package com.smartstoragear

import android.app.Application
import com.smartstoragear.core.util.OpenCvInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SmartStorageApp : Application() {
    @Inject lateinit var openCvInitializer: OpenCvInitializer

    override fun onCreate() {
        super.onCreate()
        openCvInitializer.init()
    }
}
