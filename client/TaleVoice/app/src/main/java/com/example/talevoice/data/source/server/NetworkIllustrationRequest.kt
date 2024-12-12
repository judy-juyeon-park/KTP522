package com.example.talevoice.data.source.server

import kotlinx.serialization.Serializable

@Serializable
data class NetworkIllustrationRequest(
    val story_id: String,
    val page: Int,
    val paragraph: String
)
