package com.example.talevoice.data

import androidx.lifecycle.LiveData

interface TaleRepository {

    suspend fun getTaleList() : LiveData<List<TaleListItem>>
    suspend fun getTaleItem(taleId: String)
}