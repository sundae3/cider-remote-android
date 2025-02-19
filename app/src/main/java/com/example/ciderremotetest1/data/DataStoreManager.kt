package com.example.ciderremotetest1.data

//import android.content.Context
//import androidx.datastore.preferences.PreferenceDataStoreFactory
//import androidx.datastore.preferences.createDataStore
//import androidx.datastore.preferences.preferencesKey
//import androidx.datastore.preferences.Preferences
//import androidx.datastore.preferences.edit
//import androidx.datastore.preferences.preferencesDataStore
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//
//class DataStoreManager(context: Context) {
//
//    private val dataStore = context.createDataStore(name = "app_preferences")
//
//    companion object {
//        val URL_KEY = preferencesKey<String>("user_url_key")
//    }
//
//    // Save the URL
//    suspend fun saveUrl(url: String) {
//        dataStore.edit { preferences ->
//            preferences[URL_KEY] = url
//        }
//    }
//
//    // Retrieve the URL
//    val getUrl: Flow<String> = dataStore.data
//        .map { preferences ->
//            preferences[URL_KEY] ?: ""  // Return empty string if no URL is found
//        }
//}
