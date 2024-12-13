package com.example.talevoice.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talevoice.data.IllustPrompt
import com.example.talevoice.data.TaleIllustration
import com.example.talevoice.data.TaleItem
import com.example.talevoice.data.TaleRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class TaleIllustrationViewModel(private val repository: TaleRepository) : ViewModel() {

    private val _illustrations = MutableStateFlow<List<TaleIllustration>>(emptyList())
    val illustrations: StateFlow<List<TaleIllustration>> = _illustrations

    private val _imageUrls = MutableStateFlow<List<String>>(emptyList())
    val imageUrls: StateFlow<List<String>> = _imageUrls
    private var currentJob: Job? = null

    fun updateImageUrls(taleItem: TaleItem) {
        viewModelScope.launch {
            if (taleItem.image.isNotEmpty()) {
                _imageUrls.emit(taleItem.image)
            } else {
                _illustrations.collect { illustrations ->
                    _imageUrls.emit(illustrations.map { it.image })
                }
            }
        }
    }

    fun getIllustPrompts(taleItem: TaleItem, gender: String): List<IllustPrompt> {
        return taleItem.context.mapIndexed { index, text ->
            IllustPrompt(
                page = index + 1,
                paragraph = text,
                gender = gender.toString()
            )
        }

    }

    suspend fun fetchIllustrations(requests: List<IllustPrompt>) {
        currentJob?.cancel()

        _illustrations.emit(emptyList())
        Log.d("TaleIllustrationViewModel", "fetchIllustrations")

        currentJob = viewModelScope.launch {
            repository.createIllustrations(requests)
                .onEach { illustration ->
                    Log.d("TaleIllustrationViewModel", "${illustration.page} ${illustration.image}")
                    _illustrations.emit(_illustrations.value + illustration)
                }
                .catch { exception ->
                    // 예외 처리
                    Log.e("IllustrationViewModel", "Error fetching illustrations: ${exception.message}")
                }.collect()
        }

    }

    fun cancelCreateIllustrations() {
        currentJob?.cancel()
        currentJob = null
    }


}
