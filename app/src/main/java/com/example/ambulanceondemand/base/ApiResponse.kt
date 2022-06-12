package com.example.ambulanceondemand.base

/**
 * Created By : Jonathan Darwin on July 12, 2021
 */
sealed class ApiResponse<T> {
    data class Success<T>(val data: T): ApiResponse<T>()
    data class Error<Nothing>(val exception: Exception): ApiResponse<Nothing>()

    fun isSuccess(): Boolean = this is Success

    fun get(): T = (this as Success).data
}