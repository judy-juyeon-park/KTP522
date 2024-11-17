package com.example.talevoice.data

import androidx.lifecycle.LiveData
import com.example.talevoice.data.source.local.TaleDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

class DefaultTaleRepository (
    private val localDataSource: TaleDao,
    private val dispatcher: CoroutineDispatcher,
) : TaleRepository{
    override suspend fun getTaleList(): LiveData<List<TaleListItem>> {
        return withContext(dispatcher){
            localDataSource.getTaleList()
        }
    }

    override suspend fun getTaleItem(taleId: String) {
        TODO("Not yet implemented")
    }
}