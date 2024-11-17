package com.example.talevoice.data.source.server

interface NetworkDataSource {
    suspend fun loadTaleList(): List<NetworkTaleListItem>

}