package com.skripsi.dronesimulator.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/droneTypes/all")
    fun getAllDrones(): Call<ArrayList<DroneTypes>>

    @GET("/droneHistory")
    fun getHistoriesById(
        @Query("droneId") droneId: Int,
    ): Call<ArrayList<DroneHistoryResponseItem>>

    @GET("/droneHistory/date")
    fun getHistoriesByIdRange(
        @Query("droneId") droneId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
    ): Call<ArrayList<DroneHistoryResponseItem>>

    @GET("/droneStream")
    fun getStreamById(
        @Query("droneId") droneId: Int,
    ): Call<DroneStreamResponse>

    @GET("/droneStream/name")
    fun getStreamByName(
        @Query("droneName") droneName: String,
    ): Call<DroneStreamResponse>

    @GET("/droneStream/isOnline")
    fun getAllOnlineDrones(): Call<ArrayList<DroneMapResponseItem>>
}
