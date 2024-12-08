package com.example.talevoice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talevoice.data.TaleCreation
import com.example.talevoice.data.TalePrompts
import com.example.talevoice.data.TaleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaleCreationViewModel(private val repository: TaleRepository) : ViewModel() {

    private val _isCreatingTale = MutableStateFlow(false)
    val isCreatingTale = _isCreatingTale.asStateFlow()

    private val _taleCreationResult = MutableStateFlow<TaleCreation?>(null)
    val taleCreationResult = _taleCreationResult.asStateFlow()

    private val _talePromptsResult = MutableStateFlow<TalePrompts?>(null)
    val talePromptsResult = _talePromptsResult.asStateFlow()

    fun createTale() {
        viewModelScope.launch {
            _isCreatingTale.value = true
            val result = repository.getTaleCreation()
            result.fold(
                onSuccess = { response ->
                    _taleCreationResult.value = response
                    // 동화 생성 성공 후 프롬프트 요청
                    fetchPrompts(response.taleId)
                },
                onFailure = { _ ->
                    // 에러 처리 로직
                    _isCreatingTale.value = false
                }
            )
        }
    }

    private fun fetchPrompts(taleId: String) {
        viewModelScope.launch {
            val result = repository.getTalePrompts(taleId)
            result.fold(
                onSuccess = { response ->
                    _talePromptsResult.value = response
                    _isCreatingTale.value = false
                    // promptsResponse를 활용해 UI 업데이트나 다음 화면 이동 가능
                },
                onFailure = { _ ->
                    // 에러 처리 로직
                    _isCreatingTale.value = false
                }
            )
        }
    }
}