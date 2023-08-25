package com.example.folio.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.folio.features.photo.ui.PhotoDetailScreen
import com.example.folio.features.photo.ui.PhotoListScreen
import com.example.folio.features.user.ui.UserDetailScreen
import com.example.folio.shared.state.FolioAppState

@Composable
fun AppNavHost(
    appState: FolioAppState,
    startDestination: String = Destination.PhotoList.route,
    onFinish: () -> Unit
) {

    NavHost(
        navController = appState.navController,
        startDestination = startDestination
    ) {
        composable(
            route = Destination.PhotoList.route,
            arguments = Destination.PhotoList.argument()
        ) {
            PhotoListScreen(
                modifier = Modifier.fillMaxSize(),
                onUserClick = { userId ->
                    appState.navController.navigate(
                        route = with (Destination.UserDetail) {
                            buildRouteArgument(
                                bundle = bundleOf(
                                    ARG_USER_ID to userId
                                )
                            )
                        }
                    )
                },
                onPhotoClick = { photoId ->
                    appState.navController.navigate(
                        route = with (Destination.PhotoDetail) {
                            buildRouteArgument(
                                bundle = bundleOf(
                                    ARG_PHOTO_ID to photoId
                                )
                            )
                        }
                    )
                }
            )
        }

        composable(
            route = Destination.PhotoDetail.route,
            arguments = Destination.PhotoDetail.argument()
        ) { backStackEntry ->
            backStackEntry.arguments?.let { args ->
                val photoId = with (Destination.PhotoDetail) {
                    extractArgument(ARG_PHOTO_ID, route, args) as String
                }

                PhotoDetailScreen(
                    modifier = Modifier.fillMaxSize(),
                    photoId = photoId,
                    onBackPress = {
                        if (!appState.navController.popBackStack()) {
                            onFinish()
                        }
                    }
                )
            }
        }

        composable(
            route = Destination.UserDetail.route,
            arguments = Destination.UserDetail.argument()
        ) { backStackEntry ->
            backStackEntry.arguments?.let { args ->
                val userId = with (Destination.UserDetail) {
                    extractArgument(ARG_USER_ID, route, args) as String
                }

                UserDetailScreen(
                    modifier = Modifier.fillMaxSize(),
                    userId = userId,
                    onBackPress = {
                        if (!appState.navController.popBackStack()) {
                            onFinish()
                        }
                    },
                    onPhotoClick = { photoId ->
                        appState.navController.navigate(
                            route = with (Destination.PhotoDetail) {
                                buildRouteArgument(
                                    bundle = bundleOf(
                                        ARG_PHOTO_ID to photoId
                                    )
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}