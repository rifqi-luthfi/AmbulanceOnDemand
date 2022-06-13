package com.example.ambulanceondemand.repository

import com.example.ambulanceondemand.ui.landing.model.DirectionResponses
import com.example.ambulanceondemand.ui.landing.model.HospitalResponses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("maps/api/directions/json")
    fun getDirection(@Query("origin") origin: String,
                     @Query("destination") destination: String,
                     @Query("key") apiKey: String): Call<DirectionResponses>

    @GET("maps/api/place/nearbysearch/json")
    fun getHospital(@Query("location") location: String,
                    @Query("radius") radius: Int,
                    @Query("type") type: String,
                    @Query("key") apiKey: String): Call<HospitalResponses>
}