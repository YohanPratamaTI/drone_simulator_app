package com.skripsi.dronesimulator.ui.status

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.lifecycle.ViewModelProvider
import com.skripsi.dronesimulator.Utils.DRONE_NAME
import com.skripsi.dronesimulator.databinding.ActivityPantauBinding
import com.skripsi.dronesimulator.viewmodel.MainViewModel
import com.skripsi.dronesimulator.viewmodel.ViewModelFactory

class PantauActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPantauBinding
    private lateinit var videoView: VideoView
    private lateinit var mediaControls: MediaController

    private val handler = Handler(Looper.getMainLooper())

    private var elapsedHours = 0 // Start at 14 minutes
    private var elapsedMinutes = 0 // Start at 14 minutes
    private var elapsedSeconds = 0 // Start at 31 seconds

    private lateinit var mainViewModel: MainViewModel
    private lateinit var droneName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantauBinding.inflate(layoutInflater)
        setContentView(binding.root)

        droneName = intent.getStringExtra(DRONE_NAME)!!

        setupViewModel()
        setupAction()
        startElapsedTime()
    }

    private fun setupViewModel() {
        mainViewModel= ViewModelProvider(
            this,
            ViewModelFactory(this)
        )[MainViewModel::class.java]

    }

    private fun setupAction() {

        mainViewModel.getStreamByName(droneName).observe(this){

            Log.e("PantauActivity", it!!.toString())
            //setup DroneName
            binding.tvDroneName.text = it.droneName

            //setup TimeElapsed
            val duration = it.startTime!!
            val timeParts = duration.split(":")
            elapsedHours = timeParts[0].toInt()
            elapsedMinutes = timeParts[1].toInt()
            elapsedSeconds = timeParts[2].toInt()

            //set dataStream
            Log.e("PantauActivity", it.videoPath.toString())

            setupVideoView(it.videoPath!!)
        }

    }

    private fun setupVideoView(videoPath: String) {
        videoView = binding.vvStream

        // Get the resource ID for the video using its raw resource name
        val resId = resources.getIdentifier(videoPath, "raw", packageName)

        Log.e("PantauActivity---VideoPath", videoPath) // Check the exact videoPath value
        Log.e("PantauActivity---RESID", resId.toString()) // Log the resId for debugging

        // Check if the resource ID is valid
        if (resId == 0) {
            Toast.makeText(this, "Video not found!", Toast.LENGTH_LONG).show()
            return
        }

        // Construct the URI for the video in the res/raw folder
        val videoUri = Uri.parse("android.resource://$packageName/$resId")

        try {
            if (!::mediaControls.isInitialized) {
                mediaControls = MediaController(this)
                mediaControls.setAnchorView(this.videoView)
            }

            videoView.setVideoURI(videoUri)
            videoView.requestFocus()
            videoView.start()

            videoView.setOnErrorListener { _, _, _ ->
                Toast.makeText(this, "An Error Occurred While Playing Video!", Toast.LENGTH_LONG).show()
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startElapsedTime() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                // Update time
                elapsedSeconds++
                if (elapsedSeconds >= 60) {
                    elapsedSeconds = 0
                    elapsedMinutes++
                }
                if (elapsedMinutes >= 60){
                    elapsedMinutes = 0
                    elapsedHours++
                }

                // Format the time as mm:ss and display it
                val formattedTime = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds)
                binding.tvTimeElapsed.text = formattedTime

                // Repeat every second
                handler.postDelayed(this, 1000)
            }
        }, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Stop updating when activity is destroyed
    }
}