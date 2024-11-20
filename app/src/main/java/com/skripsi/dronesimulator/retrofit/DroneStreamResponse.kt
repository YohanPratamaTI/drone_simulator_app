package com.skripsi.dronesimulator.retrofit

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DroneStreamResponse(

	@field:SerializedName("videoPath")
	val videoPath: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("droneId")
	val droneId: Int? = null,

	@field:SerializedName("streamId")
	val streamId: Int? = null,

	@field:SerializedName("droneImage")
	val droneImage: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("startTime")
	val startTime: String? = null,

	@field:SerializedName("batteryPercentage")
	val batteryPercentage: String? = null,

	@field:SerializedName("droneName")
	val droneName: String? = null,

	@field:SerializedName("height")
	val height: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
) : Parcelable
