package com.example.talevoice.data

import kotlinx.serialization.Serializable

@Serializable
data class TaleStory(
    val title: String,
    val story: List<String>
)
