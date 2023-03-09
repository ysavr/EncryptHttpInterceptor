package com.mobile.encrypthttpinterceptor.network

import android.annotation.SuppressLint
import com.mobile.encrypthttpinterceptor.BuildConfig
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiCall {

    private val encryptInterceptor by lazy {
        EncryptInterceptor
    }

    private val decryptInterceptor by lazy {
        DecryptInterceptor
    }

    private val loggingInterceptor = HttpLoggingInterceptor()

    private val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(300L, TimeUnit.SECONDS)
        .readTimeout(300L, TimeUnit.SECONDS)
        .writeTimeout(300L, TimeUnit.SECONDS)
        .pingInterval(1, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .connectionPool(ConnectionPool(0,1, TimeUnit.NANOSECONDS))
        .protocols(listOf(Protocol.HTTP_1_1))
        .addInterceptor(encryptInterceptor)
        .addInterceptor(decryptInterceptor)

    @SuppressLint("NewApi")
    private fun retrofit(): Retrofit {
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(loggingInterceptor)
        }

        return Retrofit.Builder()
            .client(okHttpClient.build())
            .baseUrl("https://f94121c4-ba12-4e22-bf82-0a9145166a7c.mock.pstmn.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun apiService(): ApiService = retrofit().create(ApiService::class.java)

}
