package com.mobile.encrypthttpinterceptor.network

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.mobile.encrypthttpinterceptor.Encryptor
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer

object EncryptInterceptor : Interceptor {
    private const val key = "SampleSecretKeys" // 128 bit key // 16 character
    private const val initVector = "SampleInitVector" // 16 bytes IV / 16 character

    @RequiresApi(Build.VERSION_CODES.O)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val rawBody = request.body

        val mediaType: MediaType? = "text/plain; charset=utf-8".toMediaTypeOrNull()
        var encryptedBody = ""

        try {
            val rawBodyStr: String = rawBody?.let { requestBodyToString(it) }.toString()
            encryptedBody = Encryptor.encrypt(key, initVector, rawBodyStr).toString()
            Log.i("ApiCall", "Raw body=> $rawBodyStr")
            Log.i("ApiCall", "Encrypted BODY=> $encryptedBody")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val body = encryptedBody.toRequestBody(mediaType)
        request = request.newBuilder().header("Content-Type", body.contentType().toString())
            .method("GET", null)
            .method(request.method, body)
            .build()

        return chain.proceed(request)
    }

    private fun requestBodyToString(requestBody: RequestBody): String {
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        return buffer.readUtf8()
    }
}