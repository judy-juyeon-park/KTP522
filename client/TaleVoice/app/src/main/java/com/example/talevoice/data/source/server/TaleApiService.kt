package com.example.talevoice.data.source.server

import com.example.talevoice.data.IllustPrompt
import com.example.talevoice.data.TaleStory
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaleApiService {

    @GET("api/tales/list")
    suspend fun getTaleList(): Response<NetworkTaleList>

    @GET("api/tales/content/{id}")
    suspend fun getTaleDetail(@Path("id") taleId: String): Response<NetworkTaleContent>

    @POST("/api/talesAI/story")
    suspend fun createTale(
        @Body request: NetworkTaleCreationRequest
    ): Response<NetworkTaleCreationResponse>

    @POST("/api/talesAI/illust")
    suspend fun createIllustration(
        @Body request: IllustPrompt
    ): Response<NetworkIllustrationResponse>

    @POST("/api/talesAI/like")
    suspend fun sendFeedback(
        @Body request: TaleStory
    ): Response<NetworkFeedbackResponse>
}
