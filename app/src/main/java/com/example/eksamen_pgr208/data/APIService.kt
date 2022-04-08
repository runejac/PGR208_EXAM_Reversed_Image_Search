package com.example.eksamen_pgr208.data

import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers(
        "connection-type: image/png"
    )
    @POST("upload")
    suspend fun postImage(@Body requestBody: RequestBody): Response<String>
}