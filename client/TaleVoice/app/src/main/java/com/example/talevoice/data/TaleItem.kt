package com.example.talevoice.data

import java.util.Date

data class TaleItem(
    val taleId: String,
    val title: String,
    val content: String,
    val createAt: Date,
    val modifiedAt: Date,
)
