package com.ismaelgr.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ismaelgr.domain.usecase.DownloadDataUseCase
import com.ismaelgr.presentation.runUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val downloadDataUseCase: DownloadDataUseCase
) : ViewModel() {
    
    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading
        .onStart { downloadData() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
    
    private fun downloadData() {
        runUseCase(flow = downloadDataUseCase(), onComplete = { _loading.update { false } })
    }
}