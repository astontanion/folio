package com.example.folio.core.network

sealed interface DataResource {
    object Idle: DataResource
    object Waiting: DataResource
    data class Success<T>(val data: T): DataResource
    data class Failure<T>(val error: Throwable): DataResource
}
