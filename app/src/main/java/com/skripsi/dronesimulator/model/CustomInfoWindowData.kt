package com.skripsi.dronesimulator.model

data class CustomInfoWindowData(
    val image: String,
    val droneName: String,
    val location: String,
    val description: String,
    val height: String? = null,
    val batteryPercentage: String? = null,
    val timeElapsed: String,
)