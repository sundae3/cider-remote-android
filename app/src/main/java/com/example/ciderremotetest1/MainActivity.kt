package com.example.ciderremotetest1

import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ciderremotetest1.repository.PlaybackRepositoryImpl
import com.example.ciderremotetest1.repository.PreferencesRepository
import com.example.ciderremotetest1.repository.SocketRepository
import com.example.ciderremotetest1.ui.components.MyApp
import com.example.ciderremotetest1.viewmodel.MainViewModel
import com.example.ciderremotetest1.viewmodel.MainViewModelFactory
//import com.example.ciderremotetest1.ui.components.MyApp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var mainViewModel: MainViewModel
    private var dataLoadJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val socketRepository = SocketRepository()
        val playbackRepository = PlaybackRepositoryImpl(this)
        val preferencesRepository = PreferencesRepository(this)

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(
            socketRepository,
            playbackRepository,
            preferencesRepository
        )).get(MainViewModel::class.java)

        // Initial load
        dataLoadJob = lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.loadInitialData()
            }
        }

        enableEdgeToEdge()

        setContent {
            CiderRemoteTest1Theme {
                MyApp(mainViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            mainViewModel.refreshDataOnResume()
        }
    }

    override fun onPause() {
        super.onPause()
        dataLoadJob?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        dataLoadJob?.cancel()
    }
}