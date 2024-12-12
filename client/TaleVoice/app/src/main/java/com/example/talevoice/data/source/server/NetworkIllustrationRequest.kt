package com.example.talevoice.data.source.server

import kotlinx.serialization.Serializable

@Serializable
data class NetworkIllustrationRequest(
    val page: Int,
    val gender: String
)
