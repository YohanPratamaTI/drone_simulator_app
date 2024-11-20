package com.skripsi.dronesimulator.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.skripsi.dronesimulator.model.UserModel
import com.skripsi.dronesimulator.model.UserPreference
import com.skripsi.dronesimulator.retrofit.ApiService
import com.skripsi.dronesimulator.model.Result.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class Repository(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
) {
    fun logout() {
        MainScope().launch {
            userPreference.logout()
        }
    }
    fun getUser(): LiveData<UserModel> {
        return userPreference.getUser().asLiveData()
    }
    fun getIsLogin(): Flow<Boolean> {
        return userPreference.isLogin()
    }


    fun loginDemo(name: String, email: String, phoneNumber: String): LiveData<com.skripsi.dronesimulator.model.Result<Boolean>> {
        val result = MutableLiveData<com.skripsi.dronesimulator.model.Result<Boolean>>()
        result.value = Loading
        MainScope().launch {
            userPreference.login(
                name = name,
                email = email,
                phoneNumber = phoneNumber
            )
        }
        result.value = Success(true)
        return result
    }

}