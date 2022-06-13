package com.example.ambulanceondemand.ui.verification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ambulanceondemand.repository.driverrepository.ApiConfigDriver
import com.example.ambulanceondemand.ui.landing.MapsViewModel
import com.example.ambulanceondemand.ui.landing.model.HospitalResponses
import com.example.ambulanceondemand.ui.verification.model.VerificationResponseX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerificationViewModel: ViewModel() {

    private val _getAmbulances = MutableLiveData<VerificationResponseX>()
    val getAmbulances: LiveData<VerificationResponseX> = _getAmbulances

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setAmbulances(province : String) {
        _isLoading.value = true
        val retrofit = ApiConfigDriver.getRetrofitClientInstance()
        retrofit.getAmbulances(province)
            .enqueue(object : Callback<VerificationResponseX> {
                override fun onResponse(
                    call: Call<VerificationResponseX>,
                    response: Response<VerificationResponseX>
                ) {
                    if (response.isSuccessful) {
//                        val responseBody = response.body()?.data?.get(0)?.namaDriver
                        _getAmbulances.postValue(response.body())
                        Log.d("berhasil cuy", "onResponse: ${response.body()?.data?.get(0)?.namaDriver}}")
//                        Log.e("<TAG>",  "Hospital berhasil $responseBody")
                    }
                }

                override fun onFailure(call: Call<VerificationResponseX>, t: Throwable) {
                    Log.e("gagal woi", t.printStackTrace().toString())
                }
            })
    }

}