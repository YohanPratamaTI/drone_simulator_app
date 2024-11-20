package com.skripsi.dronesimulator.repository

import android.content.Context
import com.skripsi.dronesimulator.model.UserPreference
import com.skripsi.dronesimulator.retrofit.ApiConfig
import com.skripsi.dronesimulator.ui.login.dataStore

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val userPreference = UserPreference.getInstance(context.dataStore)
        return Repository(apiService, userPreference)
    }
}