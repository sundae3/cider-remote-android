package com.example.ciderremotetest1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ciderremotetest1.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PreferencesViewModel(private val repository: PreferencesRepository) : ViewModel() {

    private val _userUrl = MutableStateFlow("")
    val userUrl: StateFlow<String> = _userUrl

    // Save the user input URL
    fun saveUrl(url: String) {
        viewModelScope.launch {
            repository.saveUserUrl(url)
        }
    }

    // Load the URL from DataStore
    fun loadUrl() {
        viewModelScope.launch {
            repository.getUserUrl.collect { url ->
                _userUrl.value = url
            }
        }
    }
}
