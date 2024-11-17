package com.example.talevoice.data

import java.util.Date

data class TaleListItem(
    val taleId: String,
    val title: String,
    val createAt: Date,
    val modifiedAt: Date,
)
