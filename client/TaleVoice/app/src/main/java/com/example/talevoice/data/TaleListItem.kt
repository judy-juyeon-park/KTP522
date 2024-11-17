package com.example.talevoice.data

import com.google.gson.annotations.SerializedName

data class TaleListItem(
    @SerializedName("id") val taleId: String,
    val title: String,
    val version: Int
)
