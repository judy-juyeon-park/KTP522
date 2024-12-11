package com.example.talevoice.data.source.server

import com.example.talevoice.data.TaleCreation

data class NetworkTaleCreationRespond(
    val code: String,
    val message: String,
    val data: TaleCreation
)
