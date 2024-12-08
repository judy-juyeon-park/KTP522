package com.example.talevoice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.talevoice.data.TaleItem
import com.example.talevoice.data.TaleRepository
import com.example.talevoice.data.source.server.TTSApiService

@Suppress("UNCHECKED_CAST")
class TaleListViewModelFactory(
    private val repository: TaleRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaleListViewModel(repository) as T
    }
}

@Suppress("UNCHECKED_CAST")
class TaleDetailViewModelFactory(
    private val ttsApiService: TTSApiService,
    private val taleItem: TaleItem
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaleDetailViewModel(ttsApiService, taleItem) as T
    }
}

@Suppress("UNCHECKED_CAST")
class TaleCreationViewModelFactory(
    private val repository: TaleRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaleCreationViewModel(repository) as T
    }
}