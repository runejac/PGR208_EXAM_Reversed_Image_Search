package com.example.eksamen_pgr208.data.api

import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.example.eksamen_pgr208.MainActivity
import com.example.eksamen_pgr208.common.Constants
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import java.io.File

class ApiServices {
    companion object {

        fun uploadImage(mainActivity: MainActivity, filePath: String) {

            val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()

            AndroidNetworking.upload(Constants.API_UPLOAD_URL)
                .addMultipartFile("image", File(filePath))
                .addMultipartParameter("content-type", "image/png")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .setUploadProgressListener { bytesUploaded, totalBytes ->
                    println("bytesUploaded: $bytesUploaded")
                }
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String) {
                        println("From POST response: $response")
                        mainActivity.liveData.postValue(response)
                    }

                    override fun onError(error: ANError) {
                        println("From POST error: ${error.errorBody}")
                    }
                })

        }

        fun getImages(mainActivity: MainActivity) {

            // parsing response
            val gson: Gson
            val gsonBuilder = GsonBuilder()
            gson = gsonBuilder.create()

            mainActivity.liveData.observe(mainActivity) { res ->

                if (res.isEmpty()) {
                    Toast.makeText(mainActivity, "No images found OR ERROR!", Toast.LENGTH_SHORT).show()
                } else {

                    AndroidNetworking.get(Constants.API_GET_BING)
                        .addQueryParameter("url", res)
                        .setTag("image")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsString(object : StringRequestListener {
                            override fun onResponse(response: String?) {
                                val convertedResponse = gson.fromJson(response, ImageModelResult::class.java)
                                mainActivity.liveDataGet.postValue(convertedResponse)
                            }

                            override fun onError(anError: ANError?) {
                                println("ErrorBody from GET request: ${anError?.errorBody}")
                                println("ErrorCode from GET request: ${anError?.errorCode}")
                                println("ErrorDetail from GET request: ${anError?.errorDetail}")
                            }
                        })
                }
            }
        }
    }

}