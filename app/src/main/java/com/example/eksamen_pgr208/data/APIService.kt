package com.example.eksamen_pgr208.data


import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PartMap

interface APIService {

    @Multipart
    @POST("upload")
    suspend fun uploadImage(@Body string: RequestBody): Response<String?>
}

