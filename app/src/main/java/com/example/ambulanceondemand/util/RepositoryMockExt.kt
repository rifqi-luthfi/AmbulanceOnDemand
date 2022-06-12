package com.example.ambulanceondemand.util

import com.example.ambulanceondemand.base.ApiResponse
import com.example.ambulanceondemand.base.BaseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * Created By : Jonathan Darwin on July 26, 2021
 */
suspend fun<T> BaseRepository.executeMock(
    dispatchers: CoroutineDispatcher,
    duration: Long = 1500,
    block: suspend () -> ApiResponse<T>
) : ApiResponse<T> {
    return withContext(dispatchers) {
        try {
            delay(duration)
            block()
        }
        catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }
}