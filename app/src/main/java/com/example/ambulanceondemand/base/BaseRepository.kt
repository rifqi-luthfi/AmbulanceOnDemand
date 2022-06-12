package com.example.ambulanceondemand.base
import android.util.Log
import com.example.ambulanceondemand.BuildConfig
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

abstract class BaseRepository{

    protected fun<T : BaseResponse> execute(call : Call<T>) : T {
        try{
            val response = call.execute()
            return when(response.isSuccessful){
                true -> {
                    if(BuildConfig.BUILD_TYPE.equals("debug"))
                        Log.d("<RES>", Gson().toJson(response.body()!!))
                    response.body()!!
                }
                false -> {
                    if(BuildConfig.BUILD_TYPE.equals("debug"))
                        Log.d("<RES>", response.message())
                    throw Exception()
                }
            }
        }
        catch (e : Exception){
            if(BuildConfig.BUILD_TYPE.equals("debug"))
                e.message?.let {
                    Log.d("<RES>", it)
                }
            throw e
        }
    }

    protected suspend fun<T> executeWithCatch(dispatchers: CoroutineDispatcher = Dispatchers.IO, call : Call<T>) : ApiResponse<T> {
        return withContext(dispatchers) {
            try{
                val response = call.execute()
                when(response.isSuccessful){
                    true -> {
                        if(BuildConfig.BUILD_TYPE.equals("debug"))
                            Log.d("<RES>", Gson().toJson(response.body()!!))
                        ApiResponse.Success(response.body()!!)
                    }
                    false -> {
                        if(BuildConfig.BUILD_TYPE.equals("debug"))
                            Log.d("<RES>", response.message())
                        ApiResponse.Error(Exception("Something went wrong"))
                    }
                }
            }
            catch (e : Exception){
                if(BuildConfig.BUILD_TYPE.equals("debug"))
                    e.message?.let {
                        Log.d("<RES>", it)
                    }
                ApiResponse.Error(Exception("Something went wrong"))
            }
        }
    }
}
