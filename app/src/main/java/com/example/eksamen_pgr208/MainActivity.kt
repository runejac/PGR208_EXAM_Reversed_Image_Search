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
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.androidnetworking.interfaces.UploadProgressListener
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonParser
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONStringer
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var floatingActionButton : ImageView
    private var imageFromCameraOrGallery : ImageView? = null

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

        AndroidNetworking.initialize(this)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


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

                val uploadUrl = "http://api-edu.gtl.ai/api/v1/imagesearch/upload"

                //uploadImage(uploadUrl, filePath!!)

            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Not in use ATM
/*    private fun uploadImage(uploadUrl: String, imageFile: String) {
        AndroidNetworking.post(uploadUrl)
            .addHeaders("content-type", "multipart/form-data")
            //.addHeaders("content-disposition", "multipart/form-data")
            .addBodyParameter("image", imageFile)
            .setPriority(com.androidnetworking.common.Priority.HIGH)
            .build()
            .getAsString(object : StringRequestListener{
                override fun onResponse(response: String?) {

                    if (response == "") {
                        println("response is string")
                    } else {
                        var result = response
                        println("FROM RESPONSE:rightBEFORE${result}rightAFTER")
                    }

                }
                override fun onError(anError: ANError?) {
                    println(anError.toString())
                }
            })

        //println(postRequest)
    }*/


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
                .crop()
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