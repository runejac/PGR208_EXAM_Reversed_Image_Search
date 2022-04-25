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
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.Executors


class ApiServices {
    companion object : LifecycleObserver {

        private const val TAG = "ApiServices"
        val emptyArrayListFromApiCalls : ArrayList<String> = ArrayList(3)
        val liveDataAllEndPointsCouldNotFindImages : MutableLiveData<Int> = MutableLiveData<Int>()

        fun uploadImage(mainActivity: MainActivity, filePath: String) {

            //CoroutineScope(Dispatchers.IO).launch {
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
                    //.setExecutor(Executors.newSingleThreadExecutor())
                    .build()
                    // Lambda function used
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
            //}
        }

        fun getImages(mainActivity: MainActivity) {

            // parsing response
            val gson: Gson
            val gsonBuilder = GsonBuilder()
            gson = gsonBuilder.create()
            // Lambda function used
            mainActivity.liveDataUploadImage.observe(mainActivity) { res ->

                if (res.isEmpty()) {
                    Toast.makeText(mainActivity, "No images found OR ERROR!", Toast.LENGTH_SHORT).show()
                } else {

                    //CoroutineScope(Dispatchers.IO).launch {

                        val tagNameGoogle = "google"
                        Log.i(TAG,"Starting GET request at Google...")
                        AndroidNetworking.get(Constants.API_GET_GOOGLE)
                            .addQueryParameter("url", res)
                            .setTag(tagNameGoogle)
                            .setPriority(Priority.HIGH)
                            //.setExecutor(Executors.newSingleThreadExecutor())
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
                        //}

                    //CoroutineScope(Dispatchers.IO).launch {

                        val tagNameTineye = "tineye"
                        Log.i(TAG, "Starting GET request at Tineye...")
                        AndroidNetworking.get(Constants.API_GET_TINEYE)
                            .addQueryParameter("url", res)
                            .setTag(tagNameTineye)
                            .setPriority(Priority.HIGH)
                            //.setExecutor(Executors.newSingleThreadExecutor())
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
                        //}

                    //CoroutineScope(Dispatchers.IO).launch {


                        val tagNameBing = "bing"
                        Log.i(TAG,"Starting GET request at Bing...")
                        AndroidNetworking.get(Constants.API_GET_BING)
                            .addQueryParameter("url", res)
                            .setTag(tagNameBing)
                            .setPriority(Priority.HIGH)
                            //.setExecutor(Executors.newSingleThreadExecutor())
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
                        //}
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
                    throw NullPointerException("Can't handle null arrays")
                }
                if (convertedResponse.size == 0) {
                    Log.w(TAG,"Response from $apiEndPoint is $convertedResponse")
                    Log.w(TAG, "Can not handle zero-length arrays")


                    if (convertedResponse.toString().endsWith("[]")) {

                        CoroutineScope(Dispatchers.IO).launch {
                            // litt usikker på om vi trenger denne her
                            Looper.myLooper() ?: Looper.prepare()

                            Log.i(TAG, "Hello from endswith method?")
                            emptyArrayListFromApiCalls.add(convertedResponse.toString())
                            Log.i(TAG, "Size on array from ApiServices is: $emptyArrayListFromApiCalls")


                            /**
                                Denne fungerer ikke HELT optimalt fordi det enten sendes
                                2 stk requests etter 2nd gang i samme session
                                eller at ikke arraylisten rekker å slettes før
                                den poster value til liveData i MainActivity
                                Spørsom det er noe vits å bruke liveData her, kan hende
                                det er nok med bare ArrayList, og at sjekken fortsatt skjer
                                i MainActivity
                                Skal se på det på Søndag - rune
                             */

                            if (emptyArrayListFromApiCalls.size > 2) {

                                liveDataAllEndPointsCouldNotFindImages.postValue(
                                    emptyArrayListFromApiCalls.size)

                                emptyArrayListFromApiCalls.clear()
                            }
                        }
                    }
                }

                else {
                    Log.i(TAG, "Using $apiEndPoint")
                    Log.i(TAG, "Response from $apiEndPoint is $convertedResponse")
                    mainActivity.liveDataGetImages.postValue(convertedResponse)
                    //TODO lurer faen meg på om denne her gjorde susen:
                    Log.i(TAG, "Got response from at least 1/3 providers, rest will be cancelled intentionally.")
                    AndroidNetworking.cancelAll()
                    //TODO ser mer på det på Søndag - rune
                }
            } catch (e: Exception) {
                Log.w(TAG, "Exception catched in trying to get images from api: $apiEndPoint", e)
            } finally {
                val endPointName = apiEndPoint.substring(apiEndPoint.lastIndexOf("/") + 1)
                val upperCaseOnFirstLetterEndPointName = endPointName.substring(0, 1).uppercase() + endPointName.substring(1)
                Log.i(TAG, "GET request from API: '$upperCaseOnFirstLetterEndPointName' done")
            }
        }
    }
}