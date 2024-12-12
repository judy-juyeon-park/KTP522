package com.example.talevoice.data

import kotlinx.coroutines.flow.Flow

interface TaleRepository {

    fun getTaleList() : Flow<List<TaleListItem>>
    suspend fun getTaleItem(taleId: String) : TaleItem
    suspend fun createTale(name: String, gender: String): TaleCreation
    suspend fun createIllustrations(requests: List<IllustPrompt>): Flow<TaleIllustration>
}