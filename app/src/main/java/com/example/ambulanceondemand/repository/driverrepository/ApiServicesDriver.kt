package com.example.ambulanceondemand.repository.driverrepository

import com.example.ambulanceondemand.ui.landing.ambulanceresponses.AmbulanceResponse
import com.example.ambulanceondemand.ui.verification.model.VerificationResponseX
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServicesDriver {
    @GET("ambulances")
    fun getAmbulances(
        @Query("provinsi") province: String
    ): Call<VerificationResponseX>

    @GET("closest/")
    fun getNearAmbulances(
        @Query("location") location: String,
        @Query("radius") radius: Int
    ): Call<AmbulanceResponse>
}