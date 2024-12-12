package com.example.talevoice.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talevoice.data.TaleStory
import com.example.talevoice.data.TaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaleStoryViewModel(private val repository: TaleRepository) : ViewModel() {

    private val _isCreatingTale = MutableStateFlow(false) // 로딩 상태
    val isCreatingTale: StateFlow<Boolean> = _isCreatingTale

    private val _createdTale = MutableStateFlow<TaleStory?>(null) // 생성된 동화 데이터
    val createdTale: StateFlow<TaleStory?> = _createdTale

    private val _errorMessage = MutableStateFlow<String?>(null) // 오류 메시지
    val errorMessage: StateFlow<String?> = _errorMessage

    fun createTale(name: String, gender: String) {
        _isCreatingTale.value = true
        _errorMessage.value = null

        Log.d("TaleCreationViewModel", "createTale called with name=$name and gender=$gender")

        viewModelScope.launch {
            try {
                val taleResponse = repository.createTale(name, gender)
                _createdTale.value = taleResponse
                Log.d("TaleCreationViewModel", "Tale created successfully")
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("TaleCreationViewModel", "Error creating tale: ${e.message}")
            } finally {
                _isCreatingTale.value = false
                Log.d("TaleCreationViewModel", "Tale creation process finished")
            }
        }
    }

    fun getCreatedTaleItem(): TaleStory? {
        return _createdTale.value
    }

    fun resetCreatedTale() {
        _createdTale.value = null
    }
}
