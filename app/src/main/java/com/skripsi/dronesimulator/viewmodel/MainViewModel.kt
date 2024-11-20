package com.skripsi.dronesimulator.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.skripsi.dronesimulator.retrofit.DroneTypes
import com.skripsi.dronesimulator.repository.Repository
import com.skripsi.dronesimulator.retrofit.ApiConfig
import com.skripsi.dronesimulator.retrofit.DroneHistoryResponseItem
import com.skripsi.dronesimulator.retrofit.DroneMapResponseItem
import com.skripsi.dronesimulator.retrofit.DroneStreamResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val droneRepository: Repository) : ViewModel() {

    private val apiService = ApiConfig.getApiService()

    val isLoading : MutableLiveData<Boolean> by lazy{
        MutableLiveData<Boolean>()
    }

    val listDroneTypes: MutableLiveData<ArrayList<DroneTypes>> by lazy {
        MutableLiveData<ArrayList<DroneTypes>>()
    }

    val listOnlineDrones: MutableLiveData<ArrayList<DroneMapResponseItem>> by lazy {
        MutableLiveData<ArrayList<DroneMapResponseItem>>()
    }
    fun loginDemo(username: String = "Akun Demo", email: String, phoneNumber: String = "081234567890") = droneRepository.loginDemo(username,email,phoneNumber)

    fun getUser() = droneRepository.getUser()

    fun getIsLogin() = droneRepository.getIsLogin().asLiveData()
    fun logoutDemo() = droneRepository.logout()

    fun getAllDrones(): MutableLiveData<ArrayList<DroneTypes>?> {
        val result = MutableLiveData<ArrayList<DroneTypes>?>()
        isLoading.value = true  // Start loading

        apiService.getAllDrones().enqueue(object : Callback<ArrayList<DroneTypes>> {
            override fun onResponse(
                call: Call<ArrayList<DroneTypes>>,
                response: Response<ArrayList<DroneTypes>>) {

                isLoading.value = false  // Stop loading

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.e("respon", responseBody.toString())

                    if (responseBody != null) {
                        Log.e("getAllDrones", responseBody.toString())
                        result.value = responseBody
                    } else {
                        Log.e("getAllDrones", "Response body is null")
                    }
                } else {
                    Log.e("getAllDrones", "Request failed with response $response")
                }
            }

            override fun onFailure(call: Call<ArrayList<DroneTypes>>, t: Throwable) {
                isLoading.value = false  // Stop loading
                Log.e("onFailure", t.message.toString())
            }
        })
        return result
    }

    fun getHistoriesById(droneId: Int): MutableLiveData<ArrayList<DroneHistoryResponseItem>?> {
        val result = MutableLiveData<ArrayList<DroneHistoryResponseItem>?>()
        isLoading.value = true  // Start loading

        apiService.getHistoriesById(droneId).enqueue(object : Callback<ArrayList<DroneHistoryResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<DroneHistoryResponseItem>>,
                response: Response<ArrayList<DroneHistoryResponseItem>>) {

                isLoading.value = false  // Stop loading

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.e("respon", responseBody.toString())

                    if (responseBody != null) {
                        Log.e("getAllDrones", responseBody.toString())
                        result.value = responseBody
                    } else {
                        Log.e("getAllDrones", "Response body is null")
                    }
                } else {
                    Log.e("getAllDrones", "Request failed with response $response")
                }
            }

            override fun onFailure(call: Call<ArrayList<DroneHistoryResponseItem>>, t: Throwable) {
                isLoading.value = false  // Stop loading
                Log.e("onFailure", t.message.toString())
            }
        })
        return result
    }

    fun getAllOnlineDrones(): MutableLiveData<ArrayList<DroneMapResponseItem>?> {
        val result = MutableLiveData<ArrayList<DroneMapResponseItem>?>()
        isLoading.value = true  // Start loading

        apiService.getAllOnlineDrones().enqueue(object : Callback<ArrayList<DroneMapResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<DroneMapResponseItem>>,
                response: Response<ArrayList<DroneMapResponseItem>>) {

                isLoading.value = false  // Stop loading

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.e("respon", responseBody.toString())

                    if (responseBody != null) {
                        Log.e("getAllDrones", responseBody.toString())
                        result.value = responseBody
                    } else {
                        Log.e("getAllDrones", "Response body is null")
                    }
                } else {
                    Log.e("getAllDrones", "Request failed with response $response")
                }
            }

            override fun onFailure(call: Call<ArrayList<DroneMapResponseItem>>, t: Throwable) {
                isLoading.value = false  // Stop loading
                Log.e("onFailure", t.message.toString())
            }
        })
        return result
    }

    fun getHistoriesByIdRange(id: Int, startDate: String, endDate: String): MutableLiveData<ArrayList<DroneHistoryResponseItem>?> {
        val result = MutableLiveData<ArrayList<DroneHistoryResponseItem>?>()
        isLoading.value = true  // Start loading

        apiService.getHistoriesByIdRange(id, startDate, endDate).enqueue(object : Callback<ArrayList<DroneHistoryResponseItem>> {
            override fun onResponse(
                call: Call<ArrayList<DroneHistoryResponseItem>>,
                response: Response<ArrayList<DroneHistoryResponseItem>>) {

                isLoading.value = false  // Stop loading

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.e("respon", responseBody.toString())

                    if (responseBody != null) {
                        Log.e("getAllDrones", responseBody.toString())
                        result.value = responseBody
                    } else {
                        Log.e("getAllDrones", "Response body is null")
                    }
                } else {
                    Log.e("getAllDrones", "Request failed with response $response")
                }
            }

            override fun onFailure(call: Call<ArrayList<DroneHistoryResponseItem>>, t: Throwable) {
                isLoading.value = false  // Stop loading
                Log.e("onFailure", t.message.toString())
            }
        })
        return result
    }

    fun getStreamByName(droneName: String): MutableLiveData<DroneStreamResponse?> {
        val result = MutableLiveData<DroneStreamResponse?>()
        isLoading.value = true  // Start loading

        apiService.getStreamByName(droneName).enqueue(object : Callback<DroneStreamResponse> {
            override fun onResponse(
                call: Call<DroneStreamResponse>,
                response: Response<DroneStreamResponse>) {

                isLoading.value = false  // Stop loading

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.e("getStreamByName", responseBody.toString())

                    if (responseBody != null) {
                        Log.e("getStreamByName", responseBody.toString())
                        result.value = responseBody
                    } else {
                        Log.e("getStreamByName", "Response body is null")
                    }
                } else {
                    Log.e("getStreamByName", "Request failed with response $response")
                }
            }

            override fun onFailure(call: Call<DroneStreamResponse>, t: Throwable) {
                isLoading.value = false  // Stop loading
                Log.e("onFailuregetStreamByName", t.message.toString())
            }
        })
        Log.e("=========getAllDrones Response", "$result")
        return result
    }
}