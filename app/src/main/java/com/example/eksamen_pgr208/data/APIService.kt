package com.example.eksamen_pgr208.data


import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @Multipart
    @POST("upload")
    suspend fun uploadImage(@Part body: MultipartBody.Part): Response<String?>
}

