package com.example.ciderremotetest1.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepository(private val context: Context) {

    companion object {
        val USER_URL_KEY = stringPreferencesKey("user_url_key")
    }

    // Save the user input URL
    suspend fun saveUserUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_URL_KEY] = url
        }
    }

    // Get the user URL
    val getUserUrl: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_URL_KEY] ?: ""  // Return empty string if no URL is found
        }
}

