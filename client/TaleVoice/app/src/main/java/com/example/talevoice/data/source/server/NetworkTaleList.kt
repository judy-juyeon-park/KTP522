package com.example.talevoice.data.source.server

import com.example.talevoice.data.TaleListItem

data class NetworkTaleList(
    val code: String,
    val message: String,
    val data: List<TaleListItem>,
    val total: Int,
)
