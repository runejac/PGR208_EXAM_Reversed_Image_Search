package com.example.eksamen_pgr208

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.androidnetworking.AndroidNetworking
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.eksamen_pgr208.data.api.ImageModelResult
import com.example.eksamen_pgr208.data.api.ApiServices
import com.example.eksamen_pgr208.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {


    private var imageFromCameraOrGallery : ImageView? = null
    private var btnUpload : Button? = null
    private var btnSaved : Button? = null
    private var tvIntro : TextView? = null
    private var uploadProgressbar : ProgressBar? = null
    var liveDataUploadImage : MutableLiveData<String> = MutableLiveData<String>()
    var liveDataGetImages : MutableLiveData<ImageModelResult> = MutableLiveData<ImageModelResult>()

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        AndroidNetworking.initialize(this@MainActivity, okHttpClient)

        // Controlling Fragments
        /*val navView: BottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)*/

        // getting xml components

        imageFromCameraOrGallery = binding.addedImageFromEitherCameraOrMemory
        btnUpload = binding.btnUpload
        uploadProgressbar = binding.uploadProgressBar
        tvIntro = binding.tvIntro

        // hiding elements
        btnUpload?.visibility = View.GONE
        uploadProgressbar?.visibility = View.GONE
        tvIntro?.visibility = View.VISIBLE

        // Get bottom navigation shadow be gone
        val nav : BottomNavigationView = findViewById(R.id.bottomNavigationView)
        nav.selectedItemId = R.id.home
        nav.background = null


        nav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    true
                }
                R.id.saved -> {
                    startActivity(Intent(this, SavedActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.camera -> {
                    println("Kamera")
                    showCameraAndGalleryDialog(this)
                    true
                }
                else -> {false}
            }
        }



        liveDataGetImages.observe(this){ item ->

            val imagesArray = Intent(this, ResultActivity::class.java)
            imagesArray.putExtra("images", item)
            startActivity(imagesArray)
        }

        btnSaved?.setOnClickListener {
            val takeMeToSavedActivity = Intent(this, SavedActivity::class.java)
            startActivity(takeMeToSavedActivity)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        imageChooser(resultCode, data)
    }

    private fun imageChooser(resultCode: Int, data: Intent?) {
        when (resultCode) {
            RESULT_OK -> {
                btnUpload?.visibility = View.VISIBLE
                tvIntro?.visibility = View.GONE

                val uri: Uri = data?.data!!
                val filePath = FileUriUtils.getRealPath(this, uri)
                val fileName = FileUtil.getDocumentFile(this, uri)?.name

                println("filePath: $filePath")
                println("fileName: $fileName")
                println("uri: $uri")

                Glide.with(this)
                    .load(filePath)
                    .transform(RoundedCorners(50))
                    .into(imageFromCameraOrGallery!!)

                try {
                    btnUpload?.setOnClickListener {
                        ApiServices.uploadImage(this@MainActivity, filePath!!)
                        ApiServices.getImages(this@MainActivity)
                        uploadProgressbar?.visibility = View.VISIBLE

                        Toast.makeText(
                            this@MainActivity,
                            "Please wait, searching for similar images...",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Toast.makeText(this, "Image: $fileName chosen", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Exception thrown when trying to choose image: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }


     fun showCameraAndGalleryDialog(context: Context) {
        // shows dialog (modal) to prompt the user to either choose camera or gallery
        val camOrGallDialog = Dialog(context)
        camOrGallDialog.setContentView(R.layout.dialog_camera_or_gallery)
        camOrGallDialog.setTitle("Choose source: ")

        val btnGallery : ImageButton = camOrGallDialog.findViewById(R.id.btn_gallery)
        val btnCamera : ImageButton = camOrGallDialog.findViewById(R.id.btn_camera)

        btnGallery.setOnClickListener {
            ImagePicker.with(this@MainActivity)
                .galleryOnly()
                .galleryMimeTypes(arrayOf("image/*"))
                .maxResultSize(400, 400)
                .compress(1024)
                .start()
            camOrGallDialog.dismiss()
            println("gallery clicked")
        }

        btnCamera.setOnClickListener {
            ImagePicker.with(this@MainActivity)
                .cameraOnly()
                .maxResultSize(400, 400)
                .compress(1024)
                .start()
            camOrGallDialog.dismiss()
            println("camera clicked")
        }
        camOrGallDialog.show()
    }

}