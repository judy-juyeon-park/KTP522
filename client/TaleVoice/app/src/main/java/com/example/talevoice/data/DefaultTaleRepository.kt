package com.example.talevoice.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

class DefaultTaleRepository (
    private val dispatcher: CoroutineDispatcher,
    private val scope: CoroutineScope,
) : TaleRepository{
    override suspend fun getTaleList(): LiveData<List<TaleListItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTaleItem(taleId: String) {
        TODO("Not yet implemented")
    }
}