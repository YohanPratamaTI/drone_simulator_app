package com.skripsi.dronesimulator.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.skripsi.dronesimulator.R
import com.skripsi.dronesimulator.model.CustomInfoWindowData

class CustomInfoWindowRiwayatAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    @SuppressLint("InflateParams", "SetTextI18n", "DiscouragedApi")
    override fun getInfoWindow(marker: Marker): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_info_window_riwayat, null)
        val image: ImageView = view.findViewById(R.id.tv_droneImage)
        val droneName: TextView = view.findViewById(R.id.tv_droneName)
        val locationName: TextView = view.findViewById(R.id.tv_locationName)
        val description: TextView = view.findViewById(R.id.tv_description)
        val timeElapsed: TextView = view.findViewById(R.id.tv_elapsedTime)

        val data: CustomInfoWindowData? = marker.tag as? CustomInfoWindowData

        droneName.text = data?.droneName ?: "No Title"
        locationName.text = "Lokasi: ${data?.location}"
        description.text = "Keterangan: ${data?.description}"
        timeElapsed.text = "Waktu Tempuh: ${data?.timeElapsed}"

        data?.image?.let { imagePath ->
            // Extract the resource ID from the raw folder
            val resId = context.resources.getIdentifier(imagePath, "raw", context.packageName)
            if (resId != 0) {
                val bitmap = BitmapFactory.decodeResource(context.resources, resId)
                image.setImageBitmap(bitmap)
            } else {
                image.visibility = View.GONE // Hide if not found
            }
        } ?: run {
            image.visibility = View.GONE // Hide if no path is provided
        }

        return view
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }
}
