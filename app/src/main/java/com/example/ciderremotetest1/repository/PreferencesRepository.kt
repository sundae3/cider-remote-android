package com.example.ciderremotetest1.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.ciderremotetest1.model.CiderInstanceData
import dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
//import kotlinx.serialization.json.Json

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

    private val ciderInstanceListKey = stringPreferencesKey("cider_instance_list_key")
    private val selectedInstanceKey = stringPreferencesKey("selected_instance_key")

    suspend fun saveObjectList(myObjectList: List<CiderInstanceData>) {
        val jsonString = Json.encodeToString(myObjectList)
        context.dataStore.edit { preferences ->
            preferences[ciderInstanceListKey] = jsonString
        }
    }

    suspend fun saveSelectedObject(mySelectedObject: CiderInstanceData) {
        val jsonString = Json.encodeToString(mySelectedObject)
        context.dataStore.edit { preferences ->
            preferences[selectedInstanceKey] = jsonString
        }
    }

    val objectListFlow: Flow<List<CiderInstanceData>> = context.dataStore.data
        .map { preferences ->
            val jsonString = preferences[ciderInstanceListKey] ?: "[]"
            Json.decodeFromString<List<CiderInstanceData>>(jsonString)
        }

    val selectedObjectedFlow : Flow<CiderInstanceData> = context.dataStore.data
        .map{ preferences ->
            val jsonString = preferences[selectedInstanceKey] ?: "{}"
            Json.decodeFromString<CiderInstanceData>(jsonString)
        }
}

