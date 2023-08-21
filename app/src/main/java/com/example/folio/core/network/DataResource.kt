package com.example.folio.core.network

sealed interface DataResource<T> {
    class Idle<T>: DataResource<T>
    class Waiting<T>: DataResource<T>
    class Success<T>(val data: T): DataResource<T>
    class Failure<T>(val error: Throwable): DataResource<T>
}

fun DataResource<*>.isWaiting() = this is DataResource.Waiting
fun DataResource<*>.isSuccess() = this is DataResource.Success
fun DataResource<*>.isFailure() = this is DataResource.Failure
fun DataResource<*>.isIdle() = this is DataResource.Idle