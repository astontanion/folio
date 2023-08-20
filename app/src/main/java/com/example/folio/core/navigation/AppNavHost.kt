package com.example.folio.core.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.folio.shared.state.FolioAppState

@Composable
fun AppNavHost(
    appState: FolioAppState,
    startDestination: String = Destination.PhotoList.route
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination
    ) {
        composable(
            route = Destination.PhotoList.route,
            arguments = Destination.PhotoList.argument()
        ) {
            Text(text = "Photo List")
        }

        composable(
            route = Destination.PhotoDetail.route,
            arguments = Destination.PhotoDetail.argument()
        ) {
            Text(text = "Photo Detail")
        }

        composable(
            route = Destination.UserDetail.route,
            arguments = Destination.UserDetail.argument()
        ) {
            Text(text = "User Detail")
        }
    }
}