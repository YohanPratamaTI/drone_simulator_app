package com.skripsi.dronesimulator.retrofit

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DronesItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("droneId")
	val droneId: Int? = null,

	@field:SerializedName("streamId")
	val streamId: Int? = null,

	@field:SerializedName("droneImage")
	val droneImage: String? = null,

	@field:SerializedName("typeId")
	val typeId: Int? = null,

	@field:SerializedName("isOnline")
	val isOnline: Boolean? = null,

	@field:SerializedName("droneName")
	val droneName: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
) : Parcelable

@Parcelize
data class DroneTypes(

	@field:SerializedName("cruiseSpeed")
	val cruiseSpeed: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("drones")
	val drones: ArrayList<DronesItem?>? = null,

	@field:SerializedName("flightEndurance")
	val flightEndurance: Int? = null,

	@field:SerializedName("maxHeight")
	val maxHeight: Int? = null,

	@field:SerializedName("typeName")
	val typeName: String? = null,

	@field:SerializedName("typeId")
	val typeId: Int? = null,

	@field:SerializedName("maxSpeed")
	val maxSpeed: Int? = null,

	@field:SerializedName("maxFlightRange")
	val maxFlightRange: Int? = null,

	@field:SerializedName("typeCode")
	val typeCode: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
) : Parcelable
