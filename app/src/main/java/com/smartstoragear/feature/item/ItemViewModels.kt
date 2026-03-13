package com.smartstoragear.feature.item

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartstoragear.core.data.repository.Item
import com.smartstoragear.core.data.repository.ItemRepository
import com.smartstoragear.core.data.repository.SpaceNode
import com.smartstoragear.core.data.repository.SpaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    spaceRepository: SpaceRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val spaceId = savedStateHandle.get<String>("spaceId")?.toLongOrNull() ?: 0L
    val nodes: StateFlow<List<SpaceNode>> = spaceRepository.observeNodes(spaceId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addItem(name: String, category: String, note: String, tags: List<String>, nodeId: Long?, onDone: () -> Unit) {
        viewModelScope.launch {
            itemRepository.createItem(name, category, note, tags, nodeId)
            onDone()
        }
    }
}

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    itemRepository: ItemRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val itemId = savedStateHandle.get<String>("itemId")?.toLongOrNull() ?: 0L
    val item: StateFlow<Item?> = itemRepository.observeItem(itemId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
