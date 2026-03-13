package com.smartstoragear.feature.capture

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.smartstoragear.core.data.repository.CapturedPhoto
import com.smartstoragear.core.util.BlurDetector
import com.smartstoragear.core.util.OverlapDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class CaptureViewModel @Inject constructor(
    private val blurDetector: BlurDetector,
    private val overlapDetector: OverlapDetector
) : ViewModel() {
    private val _photos = MutableStateFlow<List<CapturedPhoto>>(emptyList())
    val photos: StateFlow<List<CapturedPhoto>> = _photos.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onPhotoCaptured(uri: String, bitmap: Bitmap) {
        if (_photos.value.size >= 7) {
            _error.value = "Maximum 7 photos allowed"
            return
        }
        if (blurDetector.isBlurry(bitmap)) {
            _error.value = "Blur detected. Retake photo."
            return
        }
        val angleBucket = overlapDetector.angleBucket(_photos.value.size)
        if (overlapDetector.isDuplicateAngle(angleBucket, _photos.value.map { it.angleBucket })) {
            _error.value = "Duplicate angle detected. Move camera and retry."
            return
        }
        _photos.value = _photos.value + CapturedPhoto(uri = uri, angleBucket = angleBucket, varianceScore = blurDetector.varianceOfLaplacian(bitmap))
        _error.value = null
    }

    fun canProceed() = _photos.value.size in 3..7
}
