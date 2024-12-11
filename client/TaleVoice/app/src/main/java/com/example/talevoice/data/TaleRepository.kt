package com.example.talevoice.data

import com.example.talevoice.data.source.server.TaleCreationResponse
import kotlinx.coroutines.flow.Flow

interface TaleRepository {

    fun getTaleList() : Flow<List<TaleListItem>>
    suspend fun getTaleItem(taleId: String) : TaleItem
    suspend fun createTale(name: String, gender: String): TaleCreationResponse

}