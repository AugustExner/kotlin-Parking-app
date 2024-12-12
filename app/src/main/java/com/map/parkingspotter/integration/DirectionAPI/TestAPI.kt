package com.map.parkingspotter.integration.DirectionAPI

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Function to make the API call
fun makeApiCallTestWithOriginAndDestinationParameter(
//    originLat: String,
//    originLng: String,
//    destination: String,
    //onDistanceFetched: (Int) -> Unit  // Add a callback to return the distance
) {
    val destination = "Vejle"  // Set a fixed origin or dynamic based on your app
    val origin = "Aarhus"  // Set a fixed origin or dynamic based on your app
    val apiKey = "AIzaSyDgORILdn4tqoGRbvGsH3eKXix5LGPldi8"  // Replace with your actual API key
    val mode = "walking"  // Replace with the desired mode

    // Make the API call
    val call = DirectionsRetrofitClient.instance.getDirections(origin, mode, destination, apiKey )

    call.enqueue(object : Callback<DirectionsResponse> {
        override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
            if (response.isSuccessful) {
                val directions = response.body()
                val distance = directions?.routes?.firstOrNull()?.legs?.firstOrNull()?.distance
                distance?.let {
                    println("Success: Distance - ${it.text} (${it.value} meters)")
                    Log.v("hi", "Success: Distance - ${it.text} (${it.value} meters)")
                   // onDistanceFetched(it.value)  // Pass the distance to the callback
                }
            } else {
                println("Error: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
            println("Failure: ${t.message}")
        }
    })
}


fun makeApiCall(
    //originLat: String,
    //originLng: String,
    //destination: String,
    //onDistanceFetched: (Int) -> Unit  // Add a callback to return the distance
) {
    val origin = "Vejle"  // Set a fixed origin or dynamic based on your app
    val destination = "Vejle Rådhus"
    val apiKey = "AIzaSyDgORILdn4tqoGRbvGsH3eKXix5LGPldi8"  // Replace with your actual API key
    val mode = "walking"  // Replace with the desired mode
    // Make the API call
    val call = DirectionsRetrofitClient.instance.getDirections(origin, mode, destination, apiKey )

    call.enqueue(object : Callback<DirectionsResponse> {
        override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
            if (response.isSuccessful) {
                val directions = response.body()
                val distance = directions?.routes?.firstOrNull()?.legs?.firstOrNull()?.distance
                if (distance != null) {
                    println("Success: Distance - ${distance.text} (${distance.value} meters)")
                    Log.v("Distance", "Success: Distance - ${distance.text} (${distance.value} meters)")
                } else {
                    Log.v("Distance", "Error: Distance data is null")
                    println("Error: Distance data is null")
                }
            } else {
                Log.v("Distance", "Error 1: Response not successful. Code: ${response.code()}")
                println("Error: ${response.code()} - ${response.message()}")
                println("Error Body: ${response.errorBody()?.string()}")
            }
            Log.v("Distance", "Error 2: onResponse finished")
        }


        override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
            println("Failure: ${t.message}")
            Log.v("Distance", "Error 3")
        }
    })
}