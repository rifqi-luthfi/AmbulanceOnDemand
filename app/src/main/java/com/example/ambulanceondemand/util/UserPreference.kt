package com.example.ambulanceondemand.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.ambulanceondemand.ui.landing.model.ModelPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {
    fun getVerification(): Flow<ModelPreference> {
        return dataStore.data.map { preferences ->
            ModelPreference(
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun setVerification(user: ModelPreference) {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = user.verification
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val STATE_KEY = booleanPreferencesKey("error")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
