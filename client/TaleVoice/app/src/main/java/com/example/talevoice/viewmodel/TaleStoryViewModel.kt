package com.example.talevoice.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.talevoice.data.TaleItem
import com.example.talevoice.data.TaleStory
import com.example.talevoice.data.TaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TaleStoryViewModel(private val repository: TaleRepository) : ViewModel() {

    private val _isCreatingTale = MutableStateFlow(false) // 로딩 상태
    val isCreatingTale: StateFlow<Boolean> = _isCreatingTale

    private val _createdTale = MutableStateFlow<TaleStory?>(null) // 생성된 동화 데이터
    val createdTale: StateFlow<TaleStory?> = _createdTale

    private val _errorMessage = MutableStateFlow<String?>(null) // 오류 메시지
    val errorMessage: StateFlow<String?> = _errorMessage

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

    fun getCreatedTaleItem(): TaleStory? {
        return _createdTale.value
    }

    fun resetCreatedTale() {
        _createdTale.value = null
    }
}
