//package com.example.ciderremotetest1

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Global DataStore instance
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
