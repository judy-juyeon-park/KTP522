package com.example.talevoice.data

import kotlinx.serialization.Serializable

@Serializable
data class TaleCreation(
    val title: String,
    val story: List<String>
)
