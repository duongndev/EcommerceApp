package com.duongnd.ecommerceapp.utils


sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message = message)
    class Loading<T> : Resource<T>()
}