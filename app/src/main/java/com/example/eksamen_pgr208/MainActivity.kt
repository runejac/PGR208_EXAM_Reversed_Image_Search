package com.example.eksamen_pgr208

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.androidnetworking.AndroidNetworking
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.eksamen_pgr208.data.api.ImageModelResult
import com.example.eksamen_pgr208.data.api.ApiServices
import com.example.eksamen_pgr208.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var floatingActionButton : ImageView
    private var imageFromCameraOrGallery : ImageView? = null
    private var btnUpload : Button? = null
    private var uploadProgressbar : ProgressBar? = null
    var liveData : MutableLiveData<String> = MutableLiveData<String>()
    var liveDataGet : MutableLiveData<ImageModelResult> = MutableLiveData<ImageModelResult>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidNetworking.initialize(this@MainActivity)

        // Controlling Fragments
        val navView: BottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)



        floatingActionButton = binding.fab
        imageFromCameraOrGallery = binding.addedImageFromEitherCameraOrMemory
        btnUpload = binding.btnUpload
        uploadProgressbar = binding.uploadProgressBar

        // hiding elements
        btnUpload?.visibility = View.GONE
        uploadProgressbar?.visibility = View.GONE


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
        super.onActivityResult(requestCode, resultCode, data)
        imageChooser(resultCode, data)
    }

    private fun imageChooser(resultCode: Int, data: Intent?) {
        when (resultCode) {
            RESULT_OK -> {
                btnUpload?.visibility = View.VISIBLE

                val uri: Uri = data?.data!!
                val filePath = FileUriUtils.getRealPath(this, uri)
                val fileName = FileUtil.getDocumentFile(this, uri)?.name

                println("filePath: $filePath")
                println("fileName: $fileName")
                println("uri: $uri")

                Glide.with(this)
                    .load(filePath)
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
                .compress(200)
                .start()
            camOrGallDialog.dismiss()
            println("gallery clicked")
        }

        btnCamera.setOnClickListener {
            ImagePicker.with(this)
                .cameraOnly()
                .maxResultSize(400, 400)
                .compress(200)
                .start()
            camOrGallDialog.dismiss()
            println("camera clicked")
        }
        camOrGallDialog.show()
    }

}