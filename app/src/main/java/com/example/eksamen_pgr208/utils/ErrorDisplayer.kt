package com.example.eksamen_pgr208.utils

import android.util.Log
import android.view.View
import com.androidnetworking.error.ANError
import com.example.eksamen_pgr208.MainActivity
import com.example.eksamen_pgr208.data.api.ApiServices

class ErrorDisplayer {
    companion object {
        fun displayErrorToUserIfNoEndpointHaveResult(mainActivity: MainActivity) {
            // observing the arraylist populated, if there is populated with 3 empty
            // arrays it will prompt the user with an red error message that no results
            // were found, try another image
            ApiServices.liveDataAllEndPointsCouldNotFindImages.observe(mainActivity) { apisThatReturnedEmptyArray ->
                if (apisThatReturnedEmptyArray.equals(3)) {
                    Log.i(
                        "MainActivity",
                        "${apisThatReturnedEmptyArray}/3 API endpoints did not give any result"
                    )
                    println("from the first if: $apisThatReturnedEmptyArray")
                    mainActivity.binding.tvNoResultsFound.text = "Woops! Could not find any images from one or several providers, please choose another image and try again!"
                    mainActivity.binding.uploadProgressBar.visibility = View.GONE
                    mainActivity.binding.tvLoading.visibility = View.GONE
                    mainActivity.binding.addedImageFromEitherCameraOrMemory.visibility = View.GONE
                    mainActivity.binding.tvIntroStepTwo.visibility = View.GONE
                    mainActivity.binding.fabSearch.visibility = View.GONE
                    mainActivity.binding.tvNoResultsFound.visibility = View.VISIBLE


                    // resetting the value to 0
                    ApiServices.liveDataAllEndPointsCouldNotFindImages.value = 0

                    println("from the first if but 2nd print: $apisThatReturnedEmptyArray")
                } else if (apisThatReturnedEmptyArray < 3 || apisThatReturnedEmptyArray > 3) {
                    println("from the else: $apisThatReturnedEmptyArray")
                }
            }
        }

        fun displayErrorToUserIfNoInternet(mainActivity: MainActivity, error: ANError) {
            mainActivity.binding.tvNoInternet.text = "Woops! Check your internet connection and try again. Error code: ${error.errorCode}"
            mainActivity.binding.tvNoInternet.visibility = View.VISIBLE
            mainActivity.binding.tvLoading.visibility = View.GONE
            mainActivity.binding.uploadProgressBar.visibility = View.GONE
            mainActivity.binding.addedImageFromEitherCameraOrMemory.visibility = View.GONE
            mainActivity.binding.tvIntroStepTwo.visibility = View.GONE
            mainActivity.binding.fabSearch.visibility = View.GONE
        }

        fun displayErrorToUserEndpointFaultiness(mainActivity: MainActivity, error: ANError) {
            mainActivity.binding.tvEndpointFaultiness.text = "Woops! Error at server side, please try again later or contact service provider. " +
                    "Error code: ${error.errorCode} ${error.errorDetail}"
            mainActivity.binding.uploadProgressBar.visibility = View.GONE
            mainActivity.binding.addedImageFromEitherCameraOrMemory.visibility = View.GONE
            mainActivity.binding.tvIntroStepTwo.visibility = View.GONE
            mainActivity.binding.fabSearch.visibility = View.GONE
            mainActivity.binding.tvLoading.visibility = View.GONE
            mainActivity.binding.tvEndpointFaultiness.visibility = View.VISIBLE
        }
    }
}