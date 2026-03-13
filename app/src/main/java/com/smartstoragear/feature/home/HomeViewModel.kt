package com.smartstoragear.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartstoragear.core.data.repository.Space
import com.smartstoragear.core.data.repository.SpaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(spaceRepository: SpaceRepository) : ViewModel() {
    val spaces: StateFlow<List<Space>> = spaceRepository.observeSpaces().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
