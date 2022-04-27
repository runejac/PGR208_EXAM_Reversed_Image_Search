package com.example.eksamen_pgr208.utils

import android.util.Log
import android.view.View
import com.androidnetworking.error.ANError
import com.example.eksamen_pgr208.activities.MainActivity
import com.example.eksamen_pgr208.data.network.ApiServices

class ErrorDisplayer {
    companion object {
        fun displayErrorToUserIfNoEndpointHaveResult(mainActivity: MainActivity) {
            // observing the arraylist populated, if there is populated with 3 in size
            // it will prompt the user with an red error message telling no results
            // were found, try another image
            ApiServices.liveDataAllEndPointsCouldNotFindImages.observe(mainActivity) { apisThatReturnedEmptyArray ->
                if (apisThatReturnedEmptyArray.equals(3)) {
                    Log.i(
                        "MainActivity",
                        "${apisThatReturnedEmptyArray}/3 API endpoints did not give any result"
                    )
                    println("from the first if: $apisThatReturnedEmptyArray")
                    mainActivity.binding.tvNoResultsFound.text = "Woops!\nCould not find any images from one or several providers, please choose another image and try again!"
                    mainActivity.binding.uploadProgressBar.visibility = View.GONE
                    mainActivity.binding.tvLoading.visibility = View.GONE
                    mainActivity.binding.addedImageFromEitherCameraOrMemory.visibility = View.GONE
                    mainActivity.binding.tvIntroStepTwo.visibility = View.GONE
                    mainActivity.binding.fabSearch.visibility = View.GONE
                    mainActivity.binding.tvNoResultsFound.visibility = View.VISIBLE

                    // resetting the value to 0
                    ApiServices.liveDataAllEndPointsCouldNotFindImages.value = 0
                }
            }
        }

        fun displayErrorToUserIfNoInternet(mainActivity: MainActivity, error: ANError) {
            // either timeout or no internet error prompt to user
            mainActivity.binding.tvNoInternet.text = "Woops!\nTimeout error or check your internet connection and try again.\nError code: ${error.errorCode}"
            mainActivity.binding.tvNoInternet.visibility = View.VISIBLE
            mainActivity.binding.tvLoading.visibility = View.GONE
            mainActivity.binding.uploadProgressBar.visibility = View.GONE
            mainActivity.binding.addedImageFromEitherCameraOrMemory.visibility = View.GONE
            mainActivity.binding.tvIntroStepTwo.visibility = View.GONE
            mainActivity.binding.fabSearch.visibility = View.GONE
        }

        fun displayErrorToUserEndpointFaultiness(mainActivity: MainActivity, error: ANError) {
            // error code 500+, server side errors to prompt user
            mainActivity.binding.tvEndpointFaultiness.text = "Woops!\nError at server side, please try again later or contact service provider." +
                    "\nError code: ${error.errorCode} ${error.errorDetail}"
            mainActivity.binding.uploadProgressBar.visibility = View.GONE
            mainActivity.binding.addedImageFromEitherCameraOrMemory.visibility = View.GONE
            mainActivity.binding.tvIntroStepTwo.visibility = View.GONE
            mainActivity.binding.fabSearch.visibility = View.GONE
            mainActivity.binding.tvLoading.visibility = View.GONE
            mainActivity.binding.tvEndpointFaultiness.visibility = View.VISIBLE
        }
    }
}