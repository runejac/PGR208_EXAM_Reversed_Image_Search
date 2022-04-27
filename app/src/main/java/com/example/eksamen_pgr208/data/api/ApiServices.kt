package com.example.eksamen_pgr208.data.api

import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.example.eksamen_pgr208.MainActivity
import com.example.eksamen_pgr208.common.Constants
import com.example.eksamen_pgr208.utils.ErrorDisplayer
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class ApiServices {
    companion object : LifecycleObserver {

        private const val TAG = "ApiServices"
        // http logging
        private val okHttpClient = OkHttpClient.Builder().addNetworkInterceptor(StethoInterceptor()).build()
        private val emptyArrayListFromApiCalls : ArrayList<String> = ArrayList(3)
        private val okHttpClientTimeoutTimer = OkHttpClient().newBuilder()
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.SECONDS)
            .writeTimeout(2, TimeUnit.SECONDS)
            .build()
        val liveDataAllEndPointsCouldNotFindImages : MutableLiveData<Int> = MutableLiveData<Int>()

        fun uploadImageNetworkRequest(mainActivity: MainActivity, filePath: String) {

                Log.i(TAG,"Starting UPLOAD request...")

                AndroidNetworking.upload(Constants.API_UPLOAD_URL)
                    .addMultipartFile("image", File(filePath))
                    .addMultipartParameter("content-type", "image/png")
                    .setTag("imageUpload")
                    .setExecutor(Executors.newSingleThreadExecutor())
                    .setPriority(Priority.HIGH)
                    // fixme timeout fungerer ikke.... prøvd å sette på 5 sek, nop
                    .setOkHttpClient(okHttpClientTimeoutTimer)
                    .build()
                    .setUploadProgressListener { bytesUploaded, bytesUploadedTotal ->
                        Log.i(TAG, "Bytes uploaded: $bytesUploaded/$bytesUploadedTotal")
                        if (bytesUploaded == bytesUploadedTotal) {
                            Log.i(TAG, "Upload done!")
                        }
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

                            Log.i(TAG, "Status code: ${error.errorCode}")

                            when {
                                error.errorCode == 0 -> {
                                    // e.g. no internet etc.
                                    ErrorDisplayer.displayErrorToUserIfNoInternet(mainActivity, error)
                                    Log.e(TAG, "Error on UPLOAD request, no internet connection or a " +
                                            "fatal error are causing this error." +
                                            "\nError code: ${error.errorCode}" +
                                            "\nError body below:" +
                                            "\n${error.errorBody}", error)
                                }
                                error.errorCode in 400..499 -> {
                                    // if error is client related
                                    ErrorDisplayer.displayErrorToUserIfNoInternet(mainActivity, error)
                                    Log.e(TAG, "Error on UPLOAD request." +
                                            "\nError code: ${error.errorCode}" +
                                            "\nError body below:" +
                                            "\n${error.errorBody}", error)
                                    Log.e(TAG, "onError errorCode: ${error.errorCode}")
                                    Log.e(TAG, "onError errorBody: ${error.errorBody}")
                                    Log.e(TAG, "onError errorDetail: ${error.errorDetail}")
                                    val apiError: ApiServices = error.getErrorAsObject(ApiServices::class.java)
                                    Log.e(TAG, "Error as object from ApiServices: $apiError")
                                }
                                error.errorCode >= 500 -> {
                                    // if error is server related
                                    ErrorDisplayer.displayErrorToUserEndpointFaultiness(mainActivity, error)
                                    Log.e(TAG, "Error on UPLOAD request, faulty at server side." +
                                            "\nError code: ${error.errorCode}" +
                                            "\nError body below:\n" + error.errorBody, error)
                                }
                            }
                        }
                    })
        }


        fun getImagesNetworkRequest(mainActivity: MainActivity) {

            // parsing response
            val gson: Gson
            val gsonBuilder = GsonBuilder()
            gson = gsonBuilder.create()
            // Lambda function used
            mainActivity.liveDataUploadImage.observe(mainActivity) { res ->

                if (res.isEmpty()) {
                    Toast.makeText(mainActivity, "No images found OR ERROR!", Toast.LENGTH_SHORT).show()
                } else {

                        val tagNameGoogle = "google"
                        Log.i(TAG,"Starting GET request at Google...")
                        AndroidNetworking.get(Constants.API_GET_GOOGLE)
                            .addQueryParameter("url", res)
                            .setTag(tagNameGoogle)
                            .setExecutor(Executors.newSingleThreadExecutor())
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsString(object : StringRequestListener {
                                override fun onResponse(response: String?) {
                                    responseHandler(tagNameGoogle, gson, response, mainActivity, Constants.API_GET_GOOGLE)
                                }

                                override fun onError(anError: ANError?) {
                                    Log.e(TAG, "ErrorBody from GET request at Google: ${anError?.errorBody}")
                                    Log.e(TAG, "ErrorCode from GET request at Google: ${anError?.errorCode}")
                                    Log.e(TAG, "ErrorDetail from GET request at Google: ${anError?.errorDetail}")
                                }
                            })
                        }

                        val tagNameTineye = "tineye"
                        Log.i(TAG, "Starting GET request at Tineye...")
                        AndroidNetworking.get(Constants.API_GET_TINEYE)
                            .addQueryParameter("url", res)
                            .setTag(tagNameTineye)
                            .setExecutor(Executors.newSingleThreadExecutor())
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsString(object : StringRequestListener {
                                override fun onResponse(response: String?) {
                                    responseHandler(tagNameTineye, gson, response, mainActivity, Constants.API_GET_TINEYE)
                                }

                                override fun onError(anError: ANError?) {
                                    Log.e(TAG, "ErrorBody from GET request at Tineye: ${anError?.errorBody}")
                                    Log.e(TAG, "ErrorCode from GET request at Tineye: ${anError?.errorCode}")
                                    Log.e(TAG, "ErrorDetail from GET request at Tineye: ${anError?.errorDetail}")
                                }
                            })


                        val tagNameBing = "bing"
                        Log.i(TAG,"Starting GET request at Bing...")
                        AndroidNetworking.get(Constants.API_GET_BING)
                            .addQueryParameter("url", res)
                            .setTag(tagNameBing)
                            .setExecutor(Executors.newSingleThreadExecutor())
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsString(object : StringRequestListener {
                                override fun onResponse(response: String?) {
                                    responseHandler(tagNameBing, gson, response, mainActivity, Constants.API_GET_BING)
                                }

                                override fun onError(anError: ANError?) {
                                    Log.e(TAG, "ErrorBody from GET request at Bing: ${anError?.errorBody}")
                                    Log.e(TAG, "ErrorCode from GET request at Bing: ${anError?.errorCode}")
                                    Log.e(TAG, "ErrorDetail from GET request at Bing: ${anError?.errorDetail}")
                                }
                            })
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
                    throw NullPointerException("Can't handle null arrays")
                }
                if (convertedResponse.size == 0) {
                    Log.w(TAG,"Response from $apiEndPoint is $convertedResponse")
                    Log.w(TAG, "Can not handle zero-length arrays")


                    // checking if we are getting empty arrays from providers for further use
                    if (convertedResponse.toString().endsWith("[]")) {

                        // running this on a separate thread in the background
                        // to activate the observe at utils/ErrorDisplay.kt
                        CoroutineScope(Dispatchers.IO).launch {

                            // Elvis operator to check if it exists a looper, if not it will prepare one
                            Looper.myLooper() ?: Looper.prepare()

                            // using list here to be populated, used as a checker if we get empty array from all 3 providers
                            // telling the UI and user that no result were found
                            emptyArrayListFromApiCalls.add(convertedResponse.toString())
                            Log.i(TAG, "Size on array from ApiServices is: $emptyArrayListFromApiCalls")

                            if (emptyArrayListFromApiCalls.size > 2) {
                                liveDataAllEndPointsCouldNotFindImages.postValue(
                                    emptyArrayListFromApiCalls.size)
                                // resetting arraylist for the next search
                                emptyArrayListFromApiCalls.clear()
                                // ensures to cancel all pending requests
                                AndroidNetworking.cancelAll()
                            }
                        }
                    }
                }

                else {
                    // posting response to livedata to be used in MainActivity
                    mainActivity.liveDataGetImages.postValue(convertedResponse)
                    Log.i(TAG, "Using $apiEndPoint")
                    Log.i(TAG, "Response from $apiEndPoint is $convertedResponse")
                    Log.i(TAG, "Got response from at least 1/3 providers, rest will be cancelled intentionally.")
                    AndroidNetworking.cancelAll()
                }
            } catch (e: Exception) {
                Log.w(TAG, "Exception catched in trying to get images from api: $apiEndPoint", e)
            } finally {
                // only for logging purposes
                val endPointName = apiEndPoint.substring(apiEndPoint.lastIndexOf("/") + 1)
                val upperCaseOnFirstLetterEndPointName = endPointName.substring(0, 1).uppercase() + endPointName.substring(1)
                Log.i(TAG, "GET request from API: '$upperCaseOnFirstLetterEndPointName' done")
            }
        }
    }
}