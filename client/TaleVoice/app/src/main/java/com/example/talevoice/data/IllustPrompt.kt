package com.example.talevoice.data

import kotlinx.serialization.Serializable

@Serializable
data class IllustPrompt(
    val page: Int,
    val paragraph: String, // null인 경우 생성 중
    val gender: String
)
