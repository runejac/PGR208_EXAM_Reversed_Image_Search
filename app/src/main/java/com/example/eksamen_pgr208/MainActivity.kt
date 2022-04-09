package com.example.eksamen_pgr208

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import com.androidnetworking.interfaces.UploadProgressListener
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import java.io.File
import okhttp3.OkHttpClient


class MainActivity : AppCompatActivity() {

    private lateinit var floatingActionButton : ImageView
    private var imageFromCameraOrGallery : ImageView? = null
    private var liveData : MutableLiveData<String> = MutableLiveData<String>()

    private lateinit var title : TextView


    //TODO: Sette opp FAN (https://github.com/amitshekhariitbhu/Fast-Android-Networking)
    //      Teste endpoints i Postman
    //      Sette opp SQLite (bruke Room: https://developer.android.com/training/data-storage/room)
    //      Håndtere alle lifecycle-states


    // Good to be join ya -Stian Ihlr
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get bottom navigation shadow be gone
        var nav : BottomNavigationView = findViewById(R.id.bottomNavigationView)
        nav.background = null
        nav.menu.getItem(1).isEnabled = false


        floatingActionButton = findViewById(R.id.fab)
        imageFromCameraOrGallery = findViewById(R.id.addedImageFromEitherCameraOrMemory)

        floatingActionButton.setOnClickListener {
            showCameraAndGalleryDialog()
        }




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

    fun uploadImage(filePath: String) {

        println("filePath fra uploadImage function: $filePath")

        val uploadApiUrl = "http://api-edu.gtl.ai/api/v1/imagesearch/upload"

        CoroutineScope(Dispatchers.Main).launch {



            AndroidNetworking.initialize(this@MainActivity)

            val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()


            AndroidNetworking.upload(uploadApiUrl)
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
                        println("From response: $response")
                        liveData.postValue(response)
                    }

                    override fun onError(error: ANError) {
                        println("From error: ${error.errorBody}")
                    }
                })


            liveData.observe(this@MainActivity){ res ->
                println("result from post:$res")
            }

        }

    }


    // shows dialog (modal) to prompt the user to either choose camera or gallery
    private fun showCameraAndGalleryDialog() {
     
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
                //CROP endrer faktisk denne til JPG, FYYY CROP .crop()
                .start()

            camOrGallDialog.dismiss()

            println("gallery clicked")
        }

        btnCamera.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .maxResultSize(400, 400)
                .crop()
                .start()

            camOrGallDialog.dismiss()


            println("camera clicked")
        }

        camOrGallDialog.show()
    }


}