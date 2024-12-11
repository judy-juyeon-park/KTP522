package com.example.talevoice.data.source.server

import kotlinx.serialization.Serializable

@Serializable
data class NetworkTaleCreationRequest(
    val name: String,
    val gender: String
)
