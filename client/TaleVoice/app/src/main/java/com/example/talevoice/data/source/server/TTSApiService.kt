package com.example.talevoice.data.source.server

import okhttp3.ResponseBody
import retrofit2.http.Body

interface TTSApiService {

    // TODO ("Implement TTSApiService")
//    @Headers(
//        "Content-Type: application/ssml+xml",
//        "Ocp-Apim-Subscription-Key: {YOUR_SUBSCRIPTION_KEY}",
//        "X-Microsoft-OutputFormat: audio-16khz-32kbitrate-mono-mp3"
//    )
//    @POST("/cognitiveservices/v1")
    suspend fun synthesizeSpeech(@Body ssmlBody: String): ResponseBody
}