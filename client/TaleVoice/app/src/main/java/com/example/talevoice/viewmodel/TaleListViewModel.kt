package com.example.talevoice.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talevoice.data.TaleItem
import com.example.talevoice.data.TaleListItem
import com.example.talevoice.data.TaleRepository
import com.example.talevoice.data.TaleStory
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

    suspend fun createTale(name: String, gender: String) : TaleItem {
        Log.d("TaleCreationViewModel", "createTale called with name=$name and gender=$gender")
        val taleRespond = repository.createTale(name, gender)
        val taleItem = convertTaleStoryToTaleItem(taleRespond)
        return taleItem
    }

    private fun convertTaleStoryToTaleItem(taleStory: TaleStory): TaleItem {
        return TaleItem(
            title = taleStory.title,
            context = taleStory.story,
            image = emptyList()
        )
    }

}