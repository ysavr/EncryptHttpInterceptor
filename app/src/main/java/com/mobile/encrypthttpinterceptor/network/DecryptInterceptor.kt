package com.mobile.encrypthttpinterceptor.network

import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import com.mobile.encrypthttpinterceptor.Encryptor
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.*

object DecryptInterceptor : Interceptor {
    private const val key = "SampleSecretKeys" // 128 bit key // 16 character
    private const val initVector = "SampleInitVector" // 16 bytes IV / 16 character

    @RequiresApi(Build.VERSION_CODES.O)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        if (response.isSuccessful) {

            val newResponse = response.newBuilder()
            var contentType = response.header("Content-Type")
            if (TextUtils.isEmpty(contentType)) contentType = "application/json"

            val responseStr = response.body!!.string()
            var decryptedString: String? = null
            try {
                decryptedString = Encryptor.decrypt(key, initVector, responseStr)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Log.i("ApiCall", "Response string => $responseStr")
            Log.i("ApiCall", "Decrypted BODY=> $decryptedString")
            newResponse.body(decryptedString!!.toResponseBody(contentType?.toMediaTypeOrNull()))
            return newResponse.build()
        }
        return response
    }
}