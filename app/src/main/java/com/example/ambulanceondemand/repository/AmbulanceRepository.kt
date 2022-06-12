package com.example.ambulanceondemand.repository

import com.example.ambulanceondemand.base.BaseRepository
import kotlinx.coroutines.CoroutineDispatcher
interface IAmbulanceRepository {
//    suspend fun checkAmbulance(request: CommonBody): ApiResponse<CommonResponse>
}
class AmbulanceRepository(
    private val dispatchers: CoroutineDispatcher
) : BaseRepository(), IAmbulanceRepository{


}