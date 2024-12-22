package com.example.talevoice.data

import android.util.Log
import com.example.talevoice.data.source.local.LocalTaleListItem
import com.example.talevoice.data.source.local.TaleDao
import com.example.talevoice.data.source.server.NetworkTaleCreationRequest
import com.example.talevoice.data.source.server.TaleApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import com.example.talevoice.data.source.server.NetworkFeedbackResponse as NetworkFeedbackResponse

class DefaultTaleRepository (
    private val localDataSource: TaleDao,
    private val networkApiService: TaleApiService,
    private val dispatcher: CoroutineDispatcher,
) : TaleRepository{

    fun TaleListItem.toLocalTaleListItem(): LocalTaleListItem {
        return LocalTaleListItem(
            taleId = this.taleId,
            title = this.title,
            version = this.version
        )
    }
    fun List<TaleListItem>.toLocalTaleListItems(): List<LocalTaleListItem> {
        return this.map { it.toLocalTaleListItem() }
    }

    override fun getTaleList(): Flow<List<TaleListItem>> = flow {
        try {
            val taleList = localDataSource.getTaleList()
            emit(taleList)
            val response = networkApiService.getTaleList()
            if (response.isSuccessful){
                val taleListFromNetwork = response.body()?.data
                if (taleListFromNetwork != null){
                    emit(taleListFromNetwork)

                    localDataSource.upsertTaleList(taleListFromNetwork.toLocalTaleListItems())
                }
            } else {
                throw Exception("Network request failed: ${response.errorBody()?.string()}")
            }

        } catch (_: Exception) {
            emit(emptyList<TaleListItem>())
        }
    }.flowOn(dispatcher)

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
    override suspend fun createTale(name: String, gender: String): TaleStory {
        val request = NetworkTaleCreationRequest(name, gender)
        val response = networkApiService.createTale(request)
        try {
            if (response.isSuccessful) {
                val networkTaleCreation = response.body()
                return networkTaleCreation?.data ?: throw IllegalStateException("Response body is null")
            } else {
                throw Exception("Failed to create tale: ${response.errorBody()?.string()}")
            }
        } finally {
            response.errorBody()?.close()
        }
    }

    override suspend fun createIllustrations(requests: List<IllustPrompt>): Flow<TaleIllustration> = flow {
        Log.d("DefaultTaleRepository", requests.toString())
        for (request in requests) {
            val response = networkApiService.createIllustration(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d("DefaultTaleRepository", "Received response: Page ${it.data.page}, URL: ${it.data.image}")
                    emit(TaleIllustration(it.data.page, it.data.image))
                } ?: throw Exception("Empty response body")
            } else {
                throw Exception("Error creating illustration: ${response.errorBody()?.string()}")
            }
        }
    }.flowOn(Dispatchers.IO) // Use IO dispatcher for network operations

    override suspend fun sendFeedback(taleStory: TaleStory) : NetworkFeedbackResponse? {
        Log.d("DefaultTaleRepository", "sendFeedback: $taleStory")

        return try {
            val response = networkApiService.sendFeedback(taleStory)
            if (response.isSuccessful) {
                Log.d("DefaultTaleRepository", "Feedback sent successfully: ${response.body()}")
                response.body() // 성공 시 응답 반환
            } else {
                // response를 제대로 반환 받지 못해도 문제 없으므로 throw Exception 하지 않음
                Log.e("DefaultTaleRepository", "Failed to send feedback: ${response.errorBody()}")
                null
            }
        } catch (e: Exception) {
            Log.e("DefaultTaleRepository", "Error sending feedback", e)
            null
        }
    }
}