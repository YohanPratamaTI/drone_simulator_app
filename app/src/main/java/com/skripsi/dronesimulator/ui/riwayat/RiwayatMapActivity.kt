package com.skripsi.dronesimulator.ui.riwayat

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skripsi.dronesimulator.R
import com.skripsi.dronesimulator.Utils.DRONE_HISTORY
import com.skripsi.dronesimulator.Utils.convertToLatLngList
import com.skripsi.dronesimulator.databinding.ActivityRiwayatMapBinding
import com.skripsi.dronesimulator.model.CustomInfoWindowData
import com.skripsi.dronesimulator.retrofit.DroneHistoryResponseItem
import com.skripsi.dronesimulator.ui.CustomInfoWindowRiwayatAdapter
import com.skripsi.dronesimulator.viewmodel.MainViewModel
import com.skripsi.dronesimulator.viewmodel.ViewModelFactory

class RiwayatMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityRiwayatMapBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var mapView: MapView

    private lateinit var fab: FloatingActionButton
    private var droneMarker: Marker? = null
    private var dronePath: Polyline? = null  // Polyline to trace the path
    private val animationDuration = 5000L // Duration in milliseconds

    private lateinit var mainViewModel: MainViewModel
    private lateinit var dataRiwayat: DroneHistoryResponseItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Riwayat Map"

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        dataRiwayat = intent.getParcelableExtra<DroneHistoryResponseItem>(DRONE_HISTORY)!!

        // Initialize the FloatingActionButton
        fab = binding.fabPlayAnimation
        fab.setOnClickListener {
            playAnimation()
        }

        setupViewModel()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this)
        )[MainViewModel::class.java]
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Convert the coordinate string into a list of LatLng points
        val listDroneLatLng = dataRiwayat.coordinates!!.toString()
        val droneLatLngList = convertToLatLngList(listDroneLatLng)

        // Ensure there are at least 1 point to place the initial marker
        if (droneLatLngList.isEmpty()) return

        val firstDestination = droneLatLngList[0]

        // Place a marker at the starting position
        val customMarkerIcon = BitmapDescriptorFactory.fromResource(R.drawable.img2)
        droneMarker = googleMap.addMarker(
            MarkerOptions()
                .position(firstDestination)
                .title(dataRiwayat.droneName)
                .icon(customMarkerIcon)
        )?.apply {
            tag = CustomInfoWindowData(
                image = dataRiwayat.droneImage ?: "No Image",
                droneName = dataRiwayat.droneName ?: "No DroneName",
                location = dataRiwayat.location ?: "No Location",
                description = dataRiwayat.description ?: "No Description",
                timeElapsed = dataRiwayat.duration ?: "N/A",
            )
        }

        googleMap.setInfoWindowAdapter(CustomInfoWindowRiwayatAdapter(this))
        googleMap.setOnInfoWindowClickListener {
            val intent = Intent(this, RiwayatVideoActivity::class.java)
            intent.putExtra(DRONE_HISTORY, dataRiwayat)
            startActivity(intent)
        }

        // Move camera to the first position initially and zoom in
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstDestination, 17f))

        // Initialize polyline for drone path with all coordinates
        dronePath = googleMap.addPolyline(
            PolylineOptions()
                .addAll(droneLatLngList) // Add all points here
                .width(10f)
                .color(Color.BLUE)
                .geodesic(true)
        )
    }

    private fun playAnimation() {
        // Disable the FAB and change its color to grey
        fab.isEnabled = false
        fab.setBackgroundColor(Color.GRAY)

        // Convert the coordinate string into a list of LatLng points
        val listDroneLatLng = dataRiwayat.coordinates!!.toString()
        val droneLatLngList = convertToLatLngList(listDroneLatLng)

        // Ensure there are at least 3 points to animate
        if (droneLatLngList.size < 3) return

        // Extract the three points for the animation
        val firstDestination = droneLatLngList[0]
        val secondDestination = droneLatLngList[1]
        val finalDestination = droneLatLngList[2]

        // Initialize a list of destinations for the animation
        val destinations = listOf(firstDestination, secondDestination, finalDestination)

        // Set the polyline to show the full path
        dronePath?.points = destinations

        // Move camera to the first destination and zoom in
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstDestination, 17f))

        // Function to animate between two LatLng points
        fun animateMarker(startPoint: LatLng, endPoint: LatLng, onAnimationEnd: () -> Unit) {
            val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
            valueAnimator.duration = animationDuration

            valueAnimator.addUpdateListener { animation ->
                val v = animation.animatedFraction
                val newPosition = LatLng(
                    startPoint.latitude * (1 - v) + endPoint.latitude * v,
                    startPoint.longitude * (1 - v) + endPoint.longitude * v
                )

                // Update marker position without adding to polyline
                droneMarker?.position = newPosition
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(newPosition))
            }

            valueAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    onAnimationEnd()
                }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })

            valueAnimator.start()
        }

        // Sequentially animate the marker through each destination
        animateMarker(firstDestination, secondDestination) {
            animateMarker(secondDestination, finalDestination) {
                droneMarker?.position = finalDestination
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(finalDestination, 17f))

                // Re-enable the FAB and reset color
                fab.isEnabled = true
                fab.setBackgroundColor(Color.parseColor("#FF6200EE"))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
