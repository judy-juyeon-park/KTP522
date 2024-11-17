package com.example.talevoice.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.talevoice.data.source.local.TaleDao
import com.example.talevoice.data.source.server.TaleApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultTaleRepository (
    private val localDataSource: TaleDao,
    private val networkApiService: TaleApiService,
    private val dispatcher: CoroutineDispatcher,
) : TaleRepository{
    override fun getTaleList(): LiveData<List<TaleListItem>> {
        return liveData(dispatcher) {
            val taleList = localDataSource.getTaleList()
            emit(taleList)

            val response = networkApiService.getTaleList()
            if (response.isSuccessful){
                response.body()?.data?.let {
                    emit(it)
                }
            }
        }
    }

    override suspend fun getTaleItem(taleId: String) : TaleItem{
        return withContext(dispatcher){
            val response = networkApiService.getTaleDetail(taleId) // NetworkTaleContent 요청
            if (response.isSuccessful) {
                val networkTaleContent = response.body()
                networkTaleContent?.data ?: throw IllegalStateException("Response body is null")
            } else {
                throw Exception("Failed to fetch tale details: ${response.errorBody()?.string()}")
            }
        }
    }
}