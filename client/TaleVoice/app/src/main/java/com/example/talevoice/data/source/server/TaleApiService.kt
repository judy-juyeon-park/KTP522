package com.example.talevoice.data.source.server

import com.example.talevoice.data.TaleCreation
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

@Serializable
data class TaleCreationRequest(
    val name: String,
    val gender: String
)

data class TaleCreationResponse(
    val code: String,
    val message: String,
    val data: TaleCreation
)

interface TaleApiService {

    @GET("api/tales/list")
    suspend fun getTaleList(): Response<NetworkTaleList>

    @GET("api/tales/content/{id}")
    suspend fun getTaleDetail(@Path("id") taleId: String): Response<NetworkTaleContent>

    //@HTTP(method = "GET", path = "/api/talesAI/story", hasBody = true)
    @POST("/api/talesAI/story")
    suspend fun createTale(
        @Body request: TaleCreationRequest
    ): Response<TaleCreationResponse>
}