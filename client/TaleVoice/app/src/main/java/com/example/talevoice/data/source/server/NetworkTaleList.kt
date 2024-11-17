package com.example.talevoice.data.source.server

import com.example.talevoice.data.TaleItem
import com.example.talevoice.data.TaleListItem

data class NetworkTaleList(
    val status: String,
    val data: List<TaleListItem>,
    val total: Int,
)
