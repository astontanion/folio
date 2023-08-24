package com.example.folio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.folio.core.navigation.AppNavHost
import com.example.folio.core.network.NetworkMonitor
import com.example.folio.features.photo.usecase.RetrievePhotosUseCase
import com.example.folio.shared.state.rememberAppState
import com.example.folio.shared.ui.component.NetworkMonitorComponent
import com.example.folio.shared.ui.theme.FolioTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var retrievePhotosUseCase: RetrievePhotosUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

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
                        false -> AppNavHost(
                            appState = appState,
                            onFinish = { finish() }
                        )
                    }
                }
            }
        }
    }
}