package com.smartstoragear.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartstoragear.core.data.repository.Item
import com.smartstoragear.core.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SearchViewModel @Inject constructor(private val repo: ItemRepository) : ViewModel() {
    val query = MutableStateFlow("")
    val results: StateFlow<List<Item>> = query.flatMapLatest { repo.search(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
