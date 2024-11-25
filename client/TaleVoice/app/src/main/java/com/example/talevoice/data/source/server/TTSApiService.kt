package com.example.talevoice.data.source.server

import com.example.talevoice.BuildConfig
import okhttp3.ResponseBody
import org.simpleframework.xml.Attribute
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Headers

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "speak", strict = false)
data class SSMLRequest(
    @field:Attribute(name = "version")
    var version: String = "1.0",

    @field:Attribute(name = "xml:lang")
    var lang: String = "ko-KR",

    @field:Element(name = "voice")
    var voice: SSMLVoice
)

@Root(name = "voice", strict = false)
data class SSMLVoice(
    @field:Attribute(name = "xml:lang", required = false)
    var lang: String = "ko-KR",
    @field:Attribute(name = "xml:gender", required = false)
    var gender: String = "Female",
    @field:Attribute(name = "name", required = false)
    var name: String = "ko-KR-SunHiNeural",
    @field:Element(name = "text", required = true)
    var text: String
)

interface TTSApiService {

    @Headers(
        "Content-Type: application/ssml+xml",
        "X-Microsoft-OutputFormat: audio-16khz-32kbitrate-mono-mp3",
        "Ocp-Apim-Subscription-Key: ${BuildConfig.AZURE_TTS_API_KEY}"
    )
    @POST("/cognitiveservices/v1")
    suspend fun synthesizeSpeech(@Body body: SSMLRequest): Response<ResponseBody>


    @Headers(
        "Ocp-Apim-Subscription-Key:"+ BuildConfig.AZURE_TTS_API_KEY,
    )
    @GET("/cognitiveservices/voices/list")
    suspend fun voicesList(): Response<ResponseBody>
}