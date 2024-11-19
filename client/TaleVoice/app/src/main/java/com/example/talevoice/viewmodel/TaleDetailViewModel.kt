package com.example.talevoice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talevoice.data.TaleItem
import com.example.talevoice.data.source.server.SSMLRequest
import com.example.talevoice.data.source.server.SSMLVoice
import com.example.talevoice.data.source.server.TTSApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaleDetailViewModel(private val ttsApiService: TTSApiService, private  val taleItem: TaleItem) : ViewModel() {

    private val _pageResults = MutableStateFlow<Map<Int, ByteArray>>(emptyMap())
    val pageResults: StateFlow<Map<Int, ByteArray>> = _pageResults

    fun cleatData() {
        _pageResults.value = emptyMap<Int, ByteArray>()
    }

    fun fetchSpeech() = viewModelScope.launch {
        taleItem.context.forEachIndexed { page, text ->
            flow {
                val response = ttsApiService.synthesizeSpeech(
                    SSMLRequest(voice = SSMLVoice(text = text))
                )
                if (response.isSuccessful && response.body() != null) {
                    emit(response.body()!!.bytes())
                } else {
                    emit(null)
                }
            }
                .flowOn(Dispatchers.IO)
                .collect { result ->
                    if (result != null) {
                        _pageResults.update { currentResults ->
                            currentResults + (page to result)
                        }
                    }
                }
        }
    }

}