package com.skripsi.dronesimulator.ui.maps

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.skripsi.dronesimulator.R
import com.skripsi.dronesimulator.Utils.DRONE_NAME
import com.skripsi.dronesimulator.databinding.FragmentPantauMapBinding
import com.skripsi.dronesimulator.model.CustomInfoWindowData
import com.skripsi.dronesimulator.ui.CustomInfoWindowAdapter
import com.skripsi.dronesimulator.ui.status.PantauActivity
import com.skripsi.dronesimulator.viewmodel.MainViewModel
import com.skripsi.dronesimulator.viewmodel.ViewModelFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentPantauMapBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var mapView: MapView

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title =  "Maps Fragment"
        binding = FragmentPantauMapBinding.inflate(inflater, container, false)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        setupViewModel()
        return binding.root
    }

    private fun setupViewModel() {
        mainViewModel= ViewModelProvider(
            this,
            ViewModelFactory(requireActivity())
        )[MainViewModel::class.java]

        mainViewModel.getAllOnlineDrones().observe(viewLifecycleOwner){
            mainViewModel.listOnlineDrones.postValue(it)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Set the custom InfoWindowAdapter for displaying custom info windows
        googleMap.setInfoWindowAdapter(CustomInfoWindowAdapter(requireActivity()))

        // Observe the list of online drones and create markers for each one
        mainViewModel.listOnlineDrones.observe(viewLifecycleOwner) { droneList ->
            googleMap.clear() // Clear existing markers to prevent duplicates

            val builder = LatLngBounds.Builder()

            // Loop through each drone in the list
            droneList?.forEach { drone ->
                // Ensure coordinates are valid
                val coordinates = drone.coordinates?.split(",")
                if (coordinates != null && coordinates.size == 2) {
                    val latitude = coordinates[0].toDoubleOrNull()
                    val longitude = coordinates[1].toDoubleOrNull()

                    if (latitude != null && longitude != null) {
                        val droneLatLng = LatLng(latitude, longitude)
                        builder.include(droneLatLng) // Include in bounds

                        // Add marker for the drone
                        val marker = googleMap.addMarker(
                            MarkerOptions()
                                .position(droneLatLng)
                                .title(drone.droneName ?: "Unknown Drone")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.img2)) // Custom marker icon
                        )

                        // Set custom info window data
                        marker?.tag = CustomInfoWindowData(
                            image = drone.droneImage ?: "No Image",
                            droneName = drone.droneName ?: "No DroneName",
                            location = drone.location ?: "No Location",
                            description = drone.description ?: "No Description",
                            height = drone.height ?: "N/A",
                            batteryPercentage = drone.batteryPercentage ?: "N/A",
                            timeElapsed = drone.startTime ?: "N/A",
                        )
                    }
                }
            }

            // Adjust the camera to show all markers
            val bounds = builder.build()
            val padding = 300 // Adjust padding as needed
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            googleMap.moveCamera(cameraUpdate)
        }

        // Handle info window click event
        googleMap.setOnInfoWindowClickListener { marker ->
            val intent = Intent(context, PantauActivity::class.java)
            intent.putExtra(DRONE_NAME, (marker.tag as? CustomInfoWindowData)?.droneName)
            context?.startActivity(intent)
        }
    }

    internal inner class InfoWindowActivity : AppCompatActivity(),
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {
        override fun onMapReady(googleMap: GoogleMap) {
            // Add markers to the map and do other map setup.
            // ...
            // Set a listener for info window events.
            googleMap.setOnInfoWindowClickListener(this)
        }

        override fun onInfoWindowClick(marker: Marker) {
            Toast.makeText(
                this, "Info window clicked",
                Toast.LENGTH_SHORT
            ).show()
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

    companion object {
        @JvmStatic
        fun newInstance(p1: String, p2: String) =
            MapsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, p1)
                    putString(ARG_PARAM2, p2)
                }
            }
    }
}