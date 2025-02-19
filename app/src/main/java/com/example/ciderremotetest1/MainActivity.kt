package com.example.ciderremotetest1

import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.ciderremotetest1.repository.PlaybackRepositoryImpl
import com.example.ciderremotetest1.repository.PreferencesRepository
import com.example.ciderremotetest1.repository.SocketRepository
import com.example.ciderremotetest1.viewmodel.MainViewModel
import com.example.ciderremotetest1.viewmodel.MainViewModelFactory
import com.example.ciderremotetest1.uicomponents.MyApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //         Create the SocketRepository instance
        val socketRepository = SocketRepository()
        val playbackRepository = PlaybackRepositoryImpl(this)
        val preferencesRepository = PreferencesRepository(this)
//         Use ViewModelProvider with a custom factory to inject the repository
        val mainViewModel = ViewModelProvider(this, MainViewModelFactory(socketRepository,playbackRepository, preferencesRepository))
            .get(MainViewModel::class.java)

        enableEdgeToEdge() // Add this line
        setContent {
            CiderRemoteTest1Theme {
                MyApp(mainViewModel)
            }
        }
    }
}