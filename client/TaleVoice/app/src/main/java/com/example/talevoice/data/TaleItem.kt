package com.example.talevoice.data

data class TaleItem(
    val taleId: String,
    val title: String,
    val content: List<TalePage>
)
