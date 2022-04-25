package com.example.eksamen_pgr208.utils

import android.util.Log
import android.view.View
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
                    mainActivity.binding.uploadProgressBar.visibility = View.GONE
                    mainActivity.binding.addedImageFromEitherCameraOrMemory.visibility = View.GONE
                    mainActivity.binding.tvNoResultsFound.visibility = View.VISIBLE
                    mainActivity.binding.tvIntroStepTwo.visibility = View.GONE
                    mainActivity.binding.fabSearch.visibility = View.GONE


                    // resetting the value to 0
                    ApiServices.liveDataAllEndPointsCouldNotFindImages.value = 0

                    println("from the first if but 2nd print: $apisThatReturnedEmptyArray")
                } else if (apisThatReturnedEmptyArray < 3 || apisThatReturnedEmptyArray > 3) {
                    println("from the else: $apisThatReturnedEmptyArray")
                }
            }
        }

        fun displayErrorToUserIfNoInternet(mainActivity: MainActivity) {
                mainActivity.binding.uploadProgressBar.visibility = View.GONE
                mainActivity.binding.addedImageFromEitherCameraOrMemory.visibility = View.GONE
                mainActivity.binding.tvNoInternet.visibility = View.VISIBLE
                mainActivity.binding.tvIntroStepTwo.visibility = View.GONE
                mainActivity.binding.fabSearch.visibility = View.GONE
        }
    }
}