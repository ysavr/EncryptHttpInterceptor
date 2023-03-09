package com.mobile.encrypthttpinterceptor.network

import com.mobile.encrypthttpinterceptor.response.InterceptResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("/user-info")
    suspend fun apiDemo(
        @Body body: HashMap<String, String>,
    ): Response<InterceptResponse>
}