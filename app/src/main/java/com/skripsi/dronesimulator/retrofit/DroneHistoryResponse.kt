package com.skripsi.dronesimulator.retrofit

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DroneHistoryResponseItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("duration")
	val duration: String? = null,

	@field:SerializedName("videoPath")
	val videoPath: String? = null,

	@field:SerializedName("droneId")
	val droneId: Int? = null,

	@field:SerializedName("historyId")
	val historyId: Int? = null,

	@field:SerializedName("coordinates")
	val coordinates: String? = null,

	@field:SerializedName("droneImage")
	val droneImage: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("droneName")
	val droneName: String? = null
) : Parcelable
