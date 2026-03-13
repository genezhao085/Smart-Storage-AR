package com.smartstoragear.feature.space

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartstoragear.core.data.repository.Space
import com.smartstoragear.core.data.repository.SpaceNode
import com.smartstoragear.core.data.repository.SpaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CreateSpaceViewModel @Inject constructor(private val repository: SpaceRepository) : ViewModel() {
    fun createSpace(name: String, onCreated: (Long) -> Unit) {
        viewModelScope.launch {
            onCreated(repository.createSpace(name, null))
        }
    }
}

@HiltViewModel
class SpaceDetailViewModel @Inject constructor(
    repository: SpaceRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val spaceId = savedStateHandle.get<String>("spaceId")?.toLongOrNull() ?: 0L
    val space: StateFlow<Space?> = repository.observeSpace(spaceId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    val nodes: StateFlow<List<SpaceNode>> = repository.observeNodes(spaceId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repository: SpaceRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val spaceId = savedStateHandle.get<String>("spaceId")?.toLongOrNull() ?: 0L
    fun finalizeScan(photos: List<com.smartstoragear.core.data.repository.CapturedPhoto>, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.saveScan(spaceId, photos)
            repository.generateMockNodes(spaceId)
            onDone()
        }
    }
}
