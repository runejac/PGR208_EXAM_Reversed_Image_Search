package com.example.eksamen_pgr208.data.api

import android.util.Log
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.Executors


class ApiServices {
    companion object {

        private const val TAG = "ApiServices"

        fun uploadImage(mainActivity: MainActivity, filePath: String) {

            CoroutineScope(Dispatchers.IO).launch {
                Log.i(TAG,"Starting UPLOAD request...")

            val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()

                AndroidNetworking.upload(Constants.API_UPLOAD_URL)
                    .addMultipartFile("image", File(filePath))
                    .addMultipartParameter("content-type", "image/png")
                    .setTag("imageUpload")
                    .setPriority(Priority.HIGH)
                    .setOkHttpClient(okHttpClient)
                    .setExecutor(Executors.newSingleThreadExecutor())
                    .build()
                    .setUploadProgressListener { bytesUploaded, _ ->
                        println("bytesUploaded: $bytesUploaded")
                    }
                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            try {
                                Log.i(TAG, "From UPLOAD response: $response")
                                mainActivity.liveDataUploadImage.postValue(response)
                            } catch (e: Exception) {
                                Log.e(TAG, "Exception catched in UPLOAD request", e)
                            } finally {
                                Log.i(TAG, "UPLOAD request from API: '${Constants.API_UPLOAD_URL}' done")
                            }
                        }
                        override fun onError(error: ANError) {
                            if (error.errorCode != 0) {
                                Log.d(TAG, "onError errorCode: ${error.errorCode}")
                                Log.d(TAG, "onError errorBody: ${error.errorBody}")
                                Log.d(TAG, "onError errorDetail: ${error.errorDetail}")
                                val apiError: ApiServices = error.getErrorAsObject(ApiServices::class.java)
                                Log.d(TAG, "Error as object: $apiError")
                                Toast.makeText(mainActivity, "Error in uploading image, contact service provider", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(mainActivity, "Error in uploading image, check your internet connection", Toast.LENGTH_LONG).show()
                                Log.e(TAG, "Error on UPLOAD request, no internet connection")
                            }
                        }
                    })
            }
        }

        fun getImages(mainActivity: MainActivity) {

            // parsing response
            val gson: Gson
            val gsonBuilder = GsonBuilder()
            gson = gsonBuilder.create()

            mainActivity.liveDataUploadImage.observe(mainActivity) { res ->

                if (res.isEmpty()) {
                    Toast.makeText(mainActivity, "No images found OR ERROR!", Toast.LENGTH_SHORT).show()
                } else {

                    CoroutineScope(Dispatchers.IO).launch {

                        val tagName = "google"
                        Log.i(TAG,"Starting GET request at Google...")
                        AndroidNetworking.get(Constants.API_GET_GOOGLE)
                            .addQueryParameter("url", res)
                            .setTag(tagName)
                            .setPriority(Priority.HIGH)
                            .setExecutor(Executors.newSingleThreadExecutor())
                            .build()
                            .getAsString(object : StringRequestListener {
                                override fun onResponse(response: String?) {
                                    responseHandler(tagName, gson, response, mainActivity, Constants.API_GET_GOOGLE)
                                }

                                override fun onError(anError: ANError?) {
                                    Log.e(TAG, "ErrorBody from GET request at Google: ${anError?.errorBody}")
                                    Log.e(TAG, "ErrorCode from GET request at Google: ${anError?.errorCode}")
                                    Log.e(TAG, "ErrorDetail from GET request at Google: ${anError?.errorDetail}")
                                }
                            })
                        }

                    CoroutineScope(Dispatchers.IO).launch {

                        val tagName = "tineye"
                        Log.i(TAG, "Starting GET request at Tineye...")
                        AndroidNetworking.get(Constants.API_GET_TINEYE)
                            .addQueryParameter("url", res)
                            .setTag(tagName)
                            .setPriority(Priority.HIGH)
                            .setExecutor(Executors.newSingleThreadExecutor())
                            .build()
                            .getAsString(object : StringRequestListener {
                                override fun onResponse(response: String?) {
                                    responseHandler(tagName, gson, response, mainActivity, Constants.API_GET_TINEYE)
                                }

                                override fun onError(anError: ANError?) {
                                    Log.e(TAG, "ErrorBody from GET request at Tineye: ${anError?.errorBody}")
                                    Log.e(TAG, "ErrorCode from GET request at Tineye: ${anError?.errorCode}")
                                    Log.e(TAG, "ErrorDetail from GET request at Tineye: ${anError?.errorDetail}")
                                }
                            })
                        }

                    CoroutineScope(Dispatchers.IO).launch {


                        val tagName = "bing"
                        Log.i(TAG,"Starting GET request at Bing...")
                        AndroidNetworking.get(Constants.API_GET_BING)
                            .addQueryParameter("url", res)
                            .setTag(tagName)
                            .setPriority(Priority.HIGH)
                            .setExecutor(Executors.newSingleThreadExecutor())
                            .build()
                            .getAsString(object : StringRequestListener {
                                override fun onResponse(response: String?) {
                                    responseHandler(tagName, gson, response, mainActivity, Constants.API_GET_BING)
                                }

                                override fun onError(anError: ANError?) {
                                    Log.e(TAG, "ErrorBody from GET request at Bing: ${anError?.errorBody}")
                                    Log.e(TAG, "ErrorCode from GET request at Bing: ${anError?.errorCode}")
                                    Log.e(TAG, "ErrorDetail from GET request at Bing: ${anError?.errorDetail}")
                                }
                            })
                        }
                }
            }
        }

        private fun responseHandler(
            tagName: String,
            gson: Gson,
            response: String?,
            mainActivity: MainActivity,
            apiEndPoint: String,
        ) {

            try {
                val convertedResponse = gson.fromJson(
                    response,
                    ImageModelResult::class.java
                )
                if (convertedResponse == null) {
                    Log.w(TAG, "Response from $apiEndPoint is $convertedResponse")
                    //fixme sjekke om det er vits å cancle request
                    //AndroidNetworking.cancel(tagName)
                    throw NullPointerException("Can't handle null arrays")
                }
                if (convertedResponse.size == 0) {
                    Log.w(TAG,"Response from $apiEndPoint is $convertedResponse")
                    //fixme sjekke om det er vits å cancle request
                    //AndroidNetworking.cancel(tagName)
                    throw IllegalArgumentException("Can't handle zero-length arrays")
                } else {
                    Log.i(TAG, "Using $apiEndPoint")
                    Log.i(TAG, "Response from $apiEndPoint is $convertedResponse")
                    mainActivity.liveDataGetImages.postValue(convertedResponse)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Exception catched in trying to get images from api: $apiEndPoint", e)
                AndroidNetworking.cancel(tagName)
            } finally {
                val endPointName = apiEndPoint.substring(apiEndPoint.lastIndexOf("/") + 1)
                val upperCaseOnFirstLetterEndPointName = endPointName.substring(0, 1).uppercase() + endPointName.substring(1)
                Log.i(TAG, "GET request from API: '$upperCaseOnFirstLetterEndPointName' done")
            }
        }
    }
}