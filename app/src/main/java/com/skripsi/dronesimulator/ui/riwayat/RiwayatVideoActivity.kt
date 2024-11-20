package com.skripsi.dronesimulator.ui.riwayat

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import com.skripsi.dronesimulator.Utils
import com.skripsi.dronesimulator.databinding.ActivityRiwayatVideoBinding
import com.skripsi.dronesimulator.retrofit.DroneHistoryResponseItem

class RiwayatVideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatVideoBinding
    private lateinit var videoView: VideoView
    private lateinit var mediaControls: MediaController

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var duration: String
    private var elapsedHours = 0
    private var elapsedMinutes = 0
    private var elapsedSeconds = 0

    private lateinit var dataRiwayat: DroneHistoryResponseItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataRiwayat = intent.getParcelableExtra<DroneHistoryResponseItem>(Utils.DRONE_HISTORY)!!

        setupVideoView()
        setupAction()
        startElapsedTime()
    }

    private fun setupAction() {

        //setup DroneName
        val droneName = dataRiwayat.droneName
        binding.tvDroneName.text = droneName.toString()

        //setup Elapsed Time
        duration = dataRiwayat.duration.toString()
        val timeParts = duration.split(":")
        elapsedHours = timeParts[0].toInt()
        elapsedMinutes = timeParts[1].toInt()
        elapsedSeconds = timeParts[2].toInt()
    }

    @SuppressLint("DiscouragedApi")
    private fun setupVideoView() {
        videoView = binding.vvStream

        // Extract the videoPath from the DroneHistoryResponseItem
        val videoPath = dataRiwayat.videoPath

        // Get the resource ID for the video using its raw resource name
        val resId = resources.getIdentifier(videoPath, "raw", packageName)

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

                // Format the time as mm:ss and display it
                val formattedTime = String.format("%02d:%02d", elapsedMinutes, elapsedSeconds)
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