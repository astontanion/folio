package com.example.folio.core.navigation

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.folio.R

abstract class DestinationArguments {
    open fun argument(): List<NamedNavArgument> {
        return listOf()
    }

    open fun buildRouteArgument(bundle: Bundle): String {
        return ""
    }

    open fun extractArgument(key: String, route: String, bundle: Bundle): Any {
        return bundle[key] ?:
            throw IllegalArgumentException(
                "$key not found when extracting the argument from the route: $route."
            )
    }
}

sealed class Destination(val route: String, @StringRes val title: Int): DestinationArguments() {
    object PhotoList: Destination(
        route = "photos",
        title = R.string.photos_list_title
    ) {
        override fun buildRouteArgument(bundle: Bundle): String {
            return "photos"
        }
    }

    object PhotoDetail: Destination(
        route = "photos/{photo_id}",
        title = R.string.photo_detail_title
    ) {
        const val ARG_PHOTO_ID = "photo_id"

        override fun argument(): List<NamedNavArgument> {
            return listOf(
                navArgument(ARG_PHOTO_ID) {
                    type = NavType.StringType
                }
            )
        }

        override fun buildRouteArgument(bundle: Bundle): String {
            val photoId = extractArgument(
                key = ARG_PHOTO_ID,
                route = route,
                bundle = bundle
            )
            return "photos/$photoId"
        }
    }

    object UserDetail: Destination(
        route = "users/{user_id}",
        title = R.string.user_detail_title
    ) {
        const val ARG_USER_ID = "user_id"

        override fun argument(): List<NamedNavArgument> {
            return listOf(
                navArgument(ARG_USER_ID) {
                    type = NavType.StringType
                }
            )
        }

        override fun buildRouteArgument(bundle: Bundle): String {
            val userId = extractArgument(
                key = ARG_USER_ID,
                route = route,
                bundle = bundle
            )
            return "users/$userId"
        }
    }
}