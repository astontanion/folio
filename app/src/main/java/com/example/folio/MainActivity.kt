package com.example.folio

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.folio.core.navigation.AppNavHost
import com.example.folio.core.network.NetworkMonitor
import com.example.folio.shared.ui.component.NetworkMonitorComponent
import com.example.folio.shared.ui.theme.FolioTheme
import com.example.folio.shared.state.rememberAppState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    @Inject
    lateinit var networkMonitor: NetworkMonitor
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        var shouldShowSplashScreen: Boolean by mutableStateOf(true)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                networkMonitor.isOnLine
                    .onEach {
                        shouldShowSplashScreen = it
                    }
                    .collect()
            }
        }
        val apiKey: String = BuildConfig.FLICKR_API_KEY
        Log.d(TAG, "api key: $apiKey")

        setContent {
            FolioTheme {
                val appState = rememberAppState(networkMonitor = networkMonitor)

                val isOffLine by appState.isOffLine.collectAsStateWithLifecycle()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (isOffLine) {
                        true -> NetworkMonitorComponent(modifier = Modifier.fillMaxSize())
                        false -> AppNavHost(appState = appState)
                    }
                }
            }
        }
    }
}