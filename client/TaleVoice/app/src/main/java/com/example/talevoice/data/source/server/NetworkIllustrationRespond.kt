package com.example.talevoice.data.source.server

import com.example.talevoice.data.TaleIllustration

data class NetworkIllustrationRespond(
    val code: Int,
    val message: String,
    val data: TaleIllustration
)
