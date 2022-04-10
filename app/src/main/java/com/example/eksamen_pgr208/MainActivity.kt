package com.example.eksamen_pgr208

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.example.eksamen_pgr208.common.Constants
import com.example.eksamen_pgr208.data.api.ImageModelResult
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import okhttp3.OkHttpClient
import java.util.*
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    private lateinit var floatingActionButton : ImageView
    private var imageFromCameraOrGallery : ImageView? = null
    private var getImages : Button? = null
    private var liveData : MutableLiveData<String> = MutableLiveData<String>()
    private var liveDataGet : MutableLiveData<ImageModelResult> = MutableLiveData<ImageModelResult>()

    private lateinit var title : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get bottom navigation shadow be gone
        var nav : BottomNavigationView = findViewById(R.id.bottomNavigationView)
        nav.background = null
        nav.menu.getItem(1).isEnabled = false


        floatingActionButton = findViewById(R.id.fab)
        imageFromCameraOrGallery = findViewById(R.id.addedImageFromEitherCameraOrMemory)
        getImages = findViewById(R.id.btn_get_images)

        floatingActionButton.setOnClickListener {
            showCameraAndGalleryDialog()
        }

        getImages?.setOnClickListener {
            getImages()

            liveDataGet.observe(this){item ->

                val imagesArray = Intent(this, ResultActivity::class.java)
                imagesArray.putExtra("images", item)
                startActivity(imagesArray)

            }
        }



        AndroidNetworking.initialize(this@MainActivity)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri : Uri = data?.data!!
                val filePath = FileUriUtils.getRealPath(this, uri)
                val fileName = FileUtil.getDocumentFile(this, uri)?.name

                println("filePath: $filePath")
                println("fileName: $fileName")
                println("uri: $uri")

                Glide.with(this)
                    .load(filePath)
                    .into(imageFromCameraOrGallery!!)

                //uploadFile(uri)
                uploadImage(filePath!!)


            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage(filePath: String) {

        CoroutineScope(Dispatchers.Main).launch {

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
                    println("totalBytes: $totalBytes")
                }
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String) {
                        println("From POST response: $response")
                        liveData.postValue(response)
                    }

                    override fun onError(error: ANError) {
                        println("From POST error: ${error.errorBody}")
                    }
                })
        }

    }

    private fun getImages() {

        // parsing response
        val gson: Gson
        val gsonBuilder = GsonBuilder()
        gson = gsonBuilder.create()

        liveData.observe(this@MainActivity) { res ->

            if (res.isEmpty()) {
                Toast.makeText(this@MainActivity, "No images found OR ERROR!", Toast.LENGTH_SHORT).show()
            } else {

                AndroidNetworking.get(Constants.API_GET_BING)
                    .addQueryParameter("url", res)
                    .setTag("image")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(object : StringRequestListener {
                        override fun onResponse(response: String?) {
                            val testModel = gson.fromJson(response, ImageModelResult::class.java)
                            liveDataGet.postValue(testModel)
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


    private fun showCameraAndGalleryDialog() {
        // shows dialog (modal) to prompt the user to either choose camera or gallery
        val camOrGallDialog = Dialog(this)
        camOrGallDialog.setContentView(R.layout.dialog_camera_or_gallery)
        camOrGallDialog.setTitle("Choose source: ")

        val btnGallery : ImageButton = camOrGallDialog.findViewById(R.id.btn_gallery)
        val btnCamera : ImageButton = camOrGallDialog.findViewById(R.id.btn_camera)

        btnGallery.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .galleryMimeTypes(arrayOf("image/*"))
                .maxResultSize(400, 400)
                .start()

            camOrGallDialog.dismiss()

            println("gallery clicked")
        }

        btnCamera.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .maxResultSize(400, 400)
                .start()

            camOrGallDialog.dismiss()

            println("camera clicked")
        }

        camOrGallDialog.show()
    }
}