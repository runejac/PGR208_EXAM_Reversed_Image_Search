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
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.eksamen_pgr208.data.api.ImageModelResult
import com.example.eksamen_pgr208.data.api.ApiServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var floatingActionButton : ImageView
    private var imageFromCameraOrGallery : ImageView? = null
    private var btnUpload : Button? = null
    var liveData : MutableLiveData<String> = MutableLiveData<String>()
    var liveDataGet : MutableLiveData<ImageModelResult> = MutableLiveData<ImageModelResult>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidNetworking.initialize(this@MainActivity)

        floatingActionButton = findViewById(R.id.fab)
        imageFromCameraOrGallery = findViewById(R.id.addedImageFromEitherCameraOrMemory)
        btnUpload = findViewById(R.id.btn_upload)

        // Get bottom navigation shadow be gone
        var nav : BottomNavigationView = findViewById(R.id.bottomNavigationView)
        nav.background = null
        nav.menu.getItem(1).isEnabled = false


        floatingActionButton.setOnClickListener {
            showCameraAndGalleryDialog()
        }


        liveDataGet.observe(this){item ->

            val imagesArray = Intent(this, ResultActivity::class.java)
            imagesArray.putExtra("images", item)
            startActivity(imagesArray)

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

                CoroutineScope(Dispatchers.Main).launch {
                    btnUpload?.setOnClickListener {
                        ApiServices.uploadImage(this@MainActivity, filePath!!)
                        ApiServices.getImages(this@MainActivity)
                    }
                }

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