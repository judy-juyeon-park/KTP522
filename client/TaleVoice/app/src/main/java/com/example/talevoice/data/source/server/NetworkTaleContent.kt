package com.example.talevoice.data.source.server

import com.example.talevoice.data.TaleItem

data class NetworkTaleContent(
    val code:String,
    val message:String,
    val data:TaleItem
)
