package com.skripsi.dronesimulator.model


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                //PatientInfo
                preferences[USERNAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[PHONENUMBER_KEY] ?: "",
            )
        }
    }

     fun isLogin(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[STATE_LOGIN_KEY] ?: false
        }
    }

    suspend fun login(name: String, email: String, phoneNumber: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = name
            preferences[EMAIL_KEY] = email
            preferences[PHONENUMBER_KEY] = phoneNumber
            preferences[STATE_LOGIN_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_LOGIN_KEY] = false
            preferences[USERNAME_KEY] = ""
            preferences[EMAIL_KEY] = ""
            preferences[PHONENUMBER_KEY] = ""
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PHONENUMBER_KEY = stringPreferencesKey("phoneNumber")
        private val STATE_LOGIN_KEY = booleanPreferencesKey("state_login")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}