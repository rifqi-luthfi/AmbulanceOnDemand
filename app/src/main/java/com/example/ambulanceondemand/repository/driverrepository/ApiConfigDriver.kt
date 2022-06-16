package com.example.ambulanceondemand.repository.driverrepository

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfigDriver {
    companion object{
//        private const val BASEURL = "https://us-central1-on-demand-ambulance-166bf.cloudfunctions.net"
//        private const val BASEURL = "https://us-central1-on-demand-ambulance.cloudfunctions.net/ambulances/"
        private const val BASEURL = "https://cariambulance.masuk.id/api/"
        fun getRetrofitClientInstance() : ApiServicesDriver {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiServicesDriver::class.java)
        }
    }
}