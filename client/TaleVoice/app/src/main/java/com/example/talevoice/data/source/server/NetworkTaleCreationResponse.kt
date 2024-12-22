package com.example.talevoice.data.source.server

import com.example.talevoice.data.TaleStory

data class NetworkTaleCreationResponse(
    val code: String,
    val message: String,
    val data: TaleStory
)
