package com.example.talevoice.data.source.server

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class RetryInterceptor(private val maxRetries: Int) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        var response: Response? = null
        var attempt = 0

        while (attempt < maxRetries) {
            try {
                Log.d("RetryInterceptor", "retry $attempt")
                response = chain.proceed(request)
                if (response.isSuccessful) {
                    return response
                }
            } catch (e: IOException) {
                if (attempt >= maxRetries - 1) {
                    throw e // 최대 재시도 횟수 도달 시 예외를 던짐
                }
            }
            attempt++
        }

        // 재시도 실패 시 마지막 응답 반환
        return response ?: throw IOException("Max retries reached without success")
    }
}
