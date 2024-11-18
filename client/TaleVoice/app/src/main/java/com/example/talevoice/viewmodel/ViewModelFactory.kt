package com.example.talevoice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.talevoice.data.TaleRepository

@Suppress("UNCHECKED_CAST")
class TaleListViewModelFactory(
    private val repository: TaleRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaleListViewModel(repository) as T
    }
}