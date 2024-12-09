package com.example.talevoice.data

import kotlinx.coroutines.flow.Flow

interface TaleRepository {

    fun getTaleList() : Flow<List<TaleListItem>>
    suspend fun getTaleItem(taleId: String) : TaleItem

/*    // 추가된 메서드
    suspend fun getTaleCreation(): TaleCreation
    suspend fun getTalePrompts(taleId: String): TalePrompts*/
}