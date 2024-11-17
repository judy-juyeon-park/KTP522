package com.example.talevoice.data

import kotlinx.serialization.Serializable

@Serializable
data class TaleItem(
    val title: String,
    val context: List<String>,
    val image: List<String>
)
