package com.example.folio.shared.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.folio.core.network.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
    networkMonitor: NetworkMonitor
    ): FolioAppState {
    return remember(coroutineScope, navController, networkMonitor) {
        FolioAppState(
            coroutineScope = coroutineScope,
            navController = navController,
            networkMonitor = networkMonitor
        )
    }
}

@Stable
class FolioAppState(
    val coroutineScope: CoroutineScope,
    val navController: NavHostController,
    val networkMonitor: NetworkMonitor
) {
    val isOffLine = networkMonitor.isOnLine.map(Boolean::not).stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )
}