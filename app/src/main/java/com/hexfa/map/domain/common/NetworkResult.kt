package com.hexfa.map.domain.common

/**
 * sealed class NetworkResult -> to wrap around NetWork Response
 * &
 * Success Class that have data
 * Error Class that have message and Nullable Data
 * Loading Class that does not need any data or message
 */
sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null,
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}