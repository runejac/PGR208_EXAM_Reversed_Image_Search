package com.example.eksamen_pgr208.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.example.eksamen_pgr208.R
import com.example.eksamen_pgr208.data.network.ApiServices
import com.example.eksamen_pgr208.adapter.model.ImageResultModel
import com.example.eksamen_pgr208.databinding.ActivityMainBinding
import com.example.eksamen_pgr208.utils.AnimationCallback
import com.example.eksamen_pgr208.utils.ErrorDisplayer
import com.example.eksamen_pgr208.utils.Helpers
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


open class MainActivity : AppCompatActivity() {

    // Fab animations
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.rotate_open_anim
    ) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.rotate_close_anim
    ) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.from_bottom_anim
    ) }
    private val toBottom: Animation by lazy {AnimationUtils.loadAnimation(this,
        R.anim.to_bottom_anim
    )}


    // Variables
    private var fabClicked = false
    private var exit = false
    var liveDataUploadImage : MutableLiveData<String> = MutableLiveData<String>()
    var liveDataGetImages : MutableLiveData<ImageResultModel> = MutableLiveData<ImageResultModel>()
    private var liveDataImageSearchedOn : MutableLiveData<String>? = MutableLiveData<String>()
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AndroidNetworking.initialize(applicationContext)

        // elements to be shown or not on onCreate
        binding.uploadProgressBar.visibility = View.GONE
        binding.tvIntro.visibility = View.VISIBLE
        binding.tvIntroStepTwo.visibility = View.GONE
        binding.tvNoResultsFound.visibility = View.GONE
        binding.tvNoInternet.visibility = View.GONE
        binding.fabSearch.visibility = View.GONE
        binding.tvLoading.visibility = View.GONE
        binding.tvEndpointFaultiness.visibility = View.GONE


        // Get bottom navigation shadow be gone
        val nav : BottomNavigationView = binding.bottomNavigationView
        nav.selectedItemId = R.id.home
        nav.background = null

        // Using callback
        fab_open_plus.setOnClickListener {
            onAddButtonClicked(object: AnimationCallback {
                override fun setAnimation(fabClicked: Boolean) {
                    super.setAnimation(fabClicked)
                    if(!fabClicked) {
                        binding.fabAddImage.visibility = View.VISIBLE
                    } else {
                        binding.fabAddImage.visibility = View.INVISIBLE
                    }
                }

                override fun setVisibility(fabClicked: Boolean) {
                    super.setVisibility(fabClicked)
                    if(!fabClicked) {
                        binding.fabSearch.startAnimation(fromBottom)
                        binding.fabAddImage.startAnimation(fromBottom)
                        binding.fabOpenPlus.startAnimation(rotateOpen)
                    } else {
                        binding.fabSearch.startAnimation(toBottom)
                        binding.fabAddImage.startAnimation(toBottom)
                        binding.fabOpenPlus.startAnimation(rotateClose)
                    }
                }
            })
        }

        fab_add_image.setOnClickListener {
            Helpers.showCameraAndGalleryDialog(this)
        }

        // Lambda function used
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

        // Lambda function used
        liveDataGetImages.observe(this){ item ->

            liveDataImageSearchedOn.let {

                it?.observe(this){imageSearchedOn ->

                    val imagesArray = Intent(this, ResultActivity::class.java)
                    imagesArray.putExtra("imageresults", item)
                    imagesArray.putExtra("image_searched_on", imageSearchedOn)
                    startActivity(imagesArray)
                }
            }
        }
    }


    // Animation callback function
    private fun onAddButtonClicked(animCallback: AnimationCallback) {
        animCallback.setVisibility(fabClicked)
        animCallback.setAnimation(fabClicked)
        fabClicked = !fabClicked
    }

    override fun onDestroy() {
        super.onDestroy()
        AndroidNetworking.forceCancelAll()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageChooser(resultCode, data)
    }


    private fun imageChooser(resultCode: Int, data: Intent?) {

        when (resultCode) {
            RESULT_OK -> {

                binding.tvNoResultsFound.visibility = View.GONE
                binding.tvEndpointFaultiness.visibility = View.GONE
                binding.fabSearch.visibility = View.VISIBLE
                val (filePath, fileName) = imageChosen(data)
                try {
                    binding.fabSearch.setOnClickListener {

                        // ensures nullsafety
                        filePath?.let {
                            ApiServices.uploadImageNetworkRequest(this@MainActivity, filePath)
                            ApiServices.getImagesNetworkRequest(this@MainActivity)
                        }

                        binding.tvNoResultsFound.visibility = View.GONE
                        binding.tvEndpointFaultiness.visibility = View.GONE
                        binding.tvIntroStepTwo.visibility = View.GONE
                        binding.uploadProgressBar.visibility = View.VISIBLE
                        binding.tvLoading.visibility = View.VISIBLE


                        Toast.makeText(
                            this@MainActivity,
                            "Please wait, searching for similar images...",
                            Toast.LENGTH_LONG
                        ).show()
                        // displays error message to user if arraylist inside is 3 in size
                        ErrorDisplayer.displayErrorToUserIfNoEndpointHaveResult(this)
                    }

                    // text to be show after a new image is chosen either from gallery or camera
                    binding.tvNoResultsFound.visibility = View.GONE
                    binding.tvNoInternet.visibility = View.GONE
                    binding.tvEndpointFaultiness.visibility = View.GONE
                    binding.fabSearch.visibility = View.VISIBLE
                    binding.tvLoading.visibility = View.GONE
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
        binding.tvIntro.visibility = View.GONE
        binding.tvIntroStepTwo.visibility = View.VISIBLE
        binding.addedImageFromEitherCameraOrMemory.visibility = View.VISIBLE

        val uri: Uri = data?.data!!
        val filePath = FileUriUtils.getRealPath(this, uri)
        val fileName = FileUtil.getDocumentFile(this, uri)?.name

        // null checker
        liveDataImageSearchedOn.let {
            it?.postValue(filePath!!)
        }

        Glide.with(this)
            .load(filePath)
            .transform(RoundedCorners(30))
            .into(binding.addedImageFromEitherCameraOrMemory)
        return Pair(filePath, fileName)
    }
}