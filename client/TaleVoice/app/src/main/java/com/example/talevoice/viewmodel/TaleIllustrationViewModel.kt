package com.example.talevoice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talevoice.data.TaleIllustration
import com.example.talevoice.data.TaleRepository
import com.example.talevoice.data.source.server.NetworkIllustrationRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class TaleIllustrationviewModel(private val repository: TaleRepository) : ViewModel() {

    private val _illustrations = MutableStateFlow<List<TaleIllustration>>(emptyList())
    val illustrations: StateFlow<List<TaleIllustration>> = _illustrations

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchIllustrations(requests: List<NetworkIllustrationRequest>) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.createIllustrations(requests).collect { newIllustration ->
                    // Append the new illustration to the existing list
                    _illustrations.value = _illustrations.value + newIllustration
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
