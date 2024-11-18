package com.example.talevoice.viewmodel

import androidx.lifecycle.ViewModel
import com.example.talevoice.data.source.server.TTSApiService
import okhttp3.ResponseBody

class TaleDetailViewModel(private val ttsApiService: TTSApiService) : ViewModel() {

    // TODO("Implement t2s")
    suspend fun text2Speech(text: String) : ResponseBody{
        return ttsApiService.synthesizeSpeech(text)
    }
}