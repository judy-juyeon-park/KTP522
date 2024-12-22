package com.example.talevoice.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talevoice.data.TaleItem
import com.example.talevoice.data.TaleRepository
import com.example.talevoice.data.TaleStory
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
import java.io.File
import java.io.IOException

class TaleDetailViewModel(
    private val repository: TaleRepository,
    private val ttsApiService: TTSApiService,
    private  val taleItem: TaleItem
) : ViewModel() {

    private val _pageResults = MutableStateFlow<Map<Int, Uri>>(emptyMap())
    val pageResults: StateFlow<Map<Int, Uri>> = _pageResults

    fun cleatData() {
        _pageResults.value = emptyMap<Int, Uri>()
    }

    fun fetchSpeech(context: Context) = viewModelScope.launch {
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
                        val fileName = "${taleItem.title}_$page.mp3"
                        val fileUri = saveToFile(context, result, fileName)
                        if (fileUri != null) {
                            _pageResults.update { currentResults ->
                                currentResults + (page to fileUri)
                            }
                        }
                    }
                }
        }
    }

    private fun saveToFile(context: Context, byteArray: ByteArray, fileName: String): Uri? {
        return try {
            val file = File(context.cacheDir, fileName)

            if (file.exists()) {
                file.delete()
            }

            file.outputStream().use { output ->
                output.write(byteArray)
            }

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private val _isLiked = mutableStateOf(false)
    val isLiked: State<Boolean> get() = _isLiked

    fun sendFeedback(taleItem: TaleItem) {
        viewModelScope.launch {
            try {
                if (!_isLiked.value) {
                    val taleStory = convertToTaleStory(taleItem)
                    repository.sendFeedback(taleStory) // API 호출
                    _isLiked.value = true // 상태 업데이트
                }
            } catch (e: Exception) {
                Log.e("TaleDetailViewModel", "Error sending like feedback", e)
            }
        }
    }

    private fun convertToTaleStory(taleItem: TaleItem): TaleStory {
        return TaleStory(
            title = taleItem.title,
            story = taleItem.context
        )
    }
}