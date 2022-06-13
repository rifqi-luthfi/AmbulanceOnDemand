package com.example.ambulanceondemand.ui.landing

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ambulanceondemand.repository.ApiConfig
import com.example.ambulanceondemand.ui.landing.model.DirectionResponses
import com.example.ambulanceondemand.ui.landing.model.HospitalResponses
import com.google.android.gms.maps.model.PolylineOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel: ViewModel() {

    private val _getDestination = MutableLiveData<HospitalResponses>()
    val getDestination: LiveData<HospitalResponses> = _getDestination

    private val _getRoute = MutableLiveData<DirectionResponses>()
    val getRoute: LiveData<DirectionResponses> = _getRoute

    private val _getDuration = MutableLiveData<String>()
    val getDuration: LiveData<String> = _getDuration

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

//    val apiServices = RetrofitClient.apiServices(this)

    fun setDestination(location: String, radius: Int, type: String, key: String) {
        _isLoading.value = true
        val retrofit = ApiConfig.getRetrofitClientInstance()
        retrofit.getHospital(location, radius, type, key)
            .enqueue(object : Callback<HospitalResponses> {
                override fun onResponse(call: Call<HospitalResponses>, response: Response<HospitalResponses>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val hospitalName = responseBody?.results?.get(0)?.name
                        val hospitalLat = responseBody?.results?.get(0)?.geometry?.location?.lat.toString()
                        val hospitalLong = responseBody?.results?.get(0)?.geometry?.location?.lng.toString()
                        _getDestination.postValue(response.body())
                        Log.e(TAG, "Hospital berhasil $hospitalName")
                    }

                }

                override fun onFailure(call: Call<HospitalResponses>, t: Throwable) {
                    _isLoading.value = false
                    Log.e("hospital error", "hospital error ${t.localizedMessage}")
                }
            })
    }

    fun setRoute(origin: String, destination: String, key: String) {
        _isLoading.value = true
        val retrofit = ApiConfig.getRetrofitClientInstance()
        retrofit.getDirection(origin, destination, key)
            .enqueue(object : Callback<DirectionResponses> {
                override fun onResponse(call: Call<DirectionResponses>, response: Response<DirectionResponses>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        _getRoute.postValue(response.body())
                        Log.e(TAG, "Rute berhasil ${response.message()}")
                    }

                }

                override fun onFailure(call: Call<DirectionResponses>, t: Throwable) {
                    Log.e(TAG, "Rute gagal ${t.localizedMessage}")
                }
            })
    }

    companion object {
        private const val TAG = "MapsViewModel"
    }

}