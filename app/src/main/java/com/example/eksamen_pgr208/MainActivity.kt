package com.example.eksamen_pgr208

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.androidnetworking.AndroidNetworking
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.eksamen_pgr208.data.api.ApiServices
import com.example.eksamen_pgr208.data.api.ImageModelResult
import com.example.eksamen_pgr208.databinding.ActivityMainBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import android.widget.Toast
import java.lang.IllegalArgumentException


class MainActivity : AppCompatActivity() {

    // Fab animations
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy {AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim)}


    // Variables
    private var fabClicked = false

    private var fabOpenPlusSign : FloatingActionButton? = null
    private var fabAddImage : FloatingActionButton? = null
    private var fabSearch : FloatingActionButton? = null
    var imageFromCameraOrGallery : ImageView? = null
    var tvIntroStepOne : TextView? = null
    var tvIntroStepTwo : TextView? = null
    var tvNoResultsFound : TextView? = null
    var uploadProgressbar : ProgressBar? = null
    private var exit = false
    var liveDataUploadImage : MutableLiveData<String> = MutableLiveData<String>()
    var liveDataGetImages : MutableLiveData<ImageModelResult> = MutableLiveData<ImageModelResult>()

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()*/

        AndroidNetworking.initialize(applicationContext)

        // Controlling Fragments
        /*val navView: BottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)*/

        // getting xml components
        fabOpenPlusSign = binding.fabOpenPlus
        imageFromCameraOrGallery = binding.addedImageFromEitherCameraOrMemory
        uploadProgressbar = binding.uploadProgressBar
        tvIntroStepOne = binding.tvIntro
        tvIntroStepTwo = binding.tvIntroStepTwo
        tvNoResultsFound = binding.tvNoResultsFound
        fabSearch = binding.fabSearch
        fabAddImage = binding.fabAddImage




        // elements to be shown or not on onCreate
        uploadProgressbar?.visibility = View.GONE
        tvIntroStepOne?.visibility = View.VISIBLE
        tvIntroStepTwo?.visibility = View.GONE
        tvNoResultsFound?.visibility = View.GONE
        fabSearch?.visibility = View.GONE


        // Get bottom navigation shadow be gone
        val nav : BottomNavigationView = binding.bottomNavigationView
        nav.selectedItemId = R.id.home
        nav.background = null

        // Onclick Listeners

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

                else -> false
            }
        }

        fab_open_plus.setOnClickListener {
            println("clicked inside")
            onAddButtonClicked()
        }

        fab_add_image.setOnClickListener {
           showCameraAndGalleryDialog()
        }

        liveDataGetImages.observe(this){ item ->

            val imagesArray = Intent(this, ResultActivity::class.java)
            imagesArray.putExtra("images", item)
            startActivity(imagesArray)
        }



    }



    override fun onBackPressed() {
        super.onBackPressed()

        if (exit) {
            finish()
        } else {
            Toast.makeText(
                this, "Press back again to exit app.",
                Toast.LENGTH_LONG
            ).show()
            exit = true
        }
    }

    private fun onAddButtonClicked() {
        setVisibility(fabClicked)
        setAnimation(fabClicked)
        fabClicked = !fabClicked
    }

    private fun setAnimation(fabClicked: Boolean) {
        if(!fabClicked) {
            fabAddImage?.visibility = View.VISIBLE
        } else {
            fabAddImage?.visibility = View.INVISIBLE
        }
    }

    private fun setVisibility(fabClicked: Boolean) {
        if(!fabClicked) {
            fabSearch?.startAnimation(fromBottom)
            fabAddImage?.startAnimation(fromBottom)
            fabOpenPlusSign?.startAnimation(rotateOpen)
        } else {
            fabSearch?.startAnimation(toBottom)
            fabAddImage?.startAnimation(toBottom)
            fabOpenPlusSign?.startAnimation(rotateClose)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageChooser(resultCode, data)
    }



    private fun imageChooser(resultCode: Int, data: Intent?) {


        // TODO Et eller annet som gjør at ved andre gangs søk i samme session, sendes det dobbelt opp med requests.


        when (resultCode) {
            RESULT_OK -> {

                tvNoResultsFound?.visibility = View.GONE
                fabSearch?.visibility = View.VISIBLE


                val (filePath, fileName) = imageChosen(data)

                try {
                    fabSearch?.setOnClickListener {

                        ApiServices.uploadImage(this@MainActivity, filePath!!)
                        ApiServices.getImages(this@MainActivity)

                        tvNoResultsFound?.visibility = View.GONE
                        uploadProgressbar?.visibility = View.VISIBLE
                        tvIntroStepTwo?.visibility = View.VISIBLE


                        Toast.makeText(
                            this@MainActivity,
                            "Please wait, searching for similar images...",
                            Toast.LENGTH_LONG
                        ).show()

                        // lage when() her?? med 3 forskjellige sizes av listen

                        ApiServices.liveDataAllEndPointsCouldNotFindImages.observe(this) {apisThatReturnedEmptyArray ->
                            if(apisThatReturnedEmptyArray.equals(3)) {
                                Log.i("MainActivity", "${apisThatReturnedEmptyArray}/3 API endpoints did not give any result")
                                println("from the first if: $apisThatReturnedEmptyArray")
                                uploadProgressbar?.visibility = View.GONE
                                imageFromCameraOrGallery?.visibility = View.GONE
                                tvNoResultsFound?.visibility = View.VISIBLE
                                tvIntroStepTwo?.visibility = View.GONE
                                fabSearch?.visibility = View.GONE
                                ApiServices.liveDataAllEndPointsCouldNotFindImages.postValue(null ?: 0)
                                println("from the first if but 2nd print: $apisThatReturnedEmptyArray")
                            } else if (apisThatReturnedEmptyArray < 3 || apisThatReturnedEmptyArray > 3) {
                                println("from the else: $apisThatReturnedEmptyArray")
                            }
                        }
                    }
                    Toast.makeText(this, "Image: $fileName chosen", Toast.LENGTH_SHORT).show()

                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Exception thrown when trying to choose image: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("MainActivity", "Catched exception", e)
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                Log.wtf("MainActivity", "Error in trying to choose image occured:\n${ImagePicker.getError(data)}")
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun imageChosen(data: Intent?): Pair<String?, String?> {
        tvIntroStepOne?.visibility = View.GONE
        tvIntroStepTwo?.visibility = View.VISIBLE
        imageFromCameraOrGallery?.visibility = View.VISIBLE

        val uri: Uri = data?.data!!
        val filePath = FileUriUtils.getRealPath(this, uri)
        val fileName = FileUtil.getDocumentFile(this, uri)?.name

        Glide.with(this)
            .load(filePath)
            .transform(RoundedCorners(50))
            .into(imageFromCameraOrGallery!!)
        return Pair(filePath, fileName)
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
                .compress(1024)
                .start()
            camOrGallDialog.dismiss()

            println("gallery clicked")
        }

        btnCamera.setOnClickListener {
            ImagePicker.with(this)
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