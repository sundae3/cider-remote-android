package com.example.ciderremotetest1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ciderremotetest1.repository.PlaybackRepository
import com.example.ciderremotetest1.repository.PreferencesRepository
import com.example.ciderremotetest1.repository.SocketRepository

class MainViewModelFactory(private val socketRepository: SocketRepository, private val playbackRepository: PlaybackRepository, private val preferencesRepository: PreferencesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(socketRepository,playbackRepository, preferencesRepository) as T
    }
}