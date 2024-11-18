package com.example.talevoice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talevoice.data.TaleItem
import com.example.talevoice.data.TaleListItem
import com.example.talevoice.data.TaleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class TaleListViewModel(private val repository: TaleRepository) : ViewModel() {

    val taleList: StateFlow<List<TaleListItem>> = repository.getTaleList()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    suspend fun getTaleDetail(taleId: String) :TaleItem {
        return repository.getTaleItem(taleId)
    }
}