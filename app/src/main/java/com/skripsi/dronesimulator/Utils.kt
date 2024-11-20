package com.skripsi.dronesimulator

import android.annotation.SuppressLint
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    @SuppressLint("ConstantLocale")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    @SuppressLint("ConstantLocale")
    val dateFormatArgs = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    fun convertToLatLngList(coordinateString: String): List<LatLng> {
        // Remove the outer square brackets
        val cleanedString = coordinateString.trim('[', ']')

        // Split the string into coordinate pairs
        val coordinatePairs = cleanedString.split("],[")

        // Map each coordinate pair to a LatLng object
        return coordinatePairs.map { pair ->
            val coords = pair.split(",")
            val lat = coords[0].toDouble()
            val lon = coords[1].toDouble()
            LatLng(lat, lon)
        }
    }
    const val DRONE_NAME = "droneName"
    const val DRONE_ITEM = "droneItem"
    const val DRONE_HISTORY = "history"
}