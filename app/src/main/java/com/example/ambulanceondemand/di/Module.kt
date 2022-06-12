package com.example.ambulanceondemand.di

import android.content.Context
import com.example.ambulanceondemand.repository.AmbulanceRepository
import com.example.ambulanceondemand.repository.IAmbulanceRepository
import com.example.ambulanceondemand.ui.landing.MapsViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    val provideInterceptor =
        Interceptor { chain ->
            val request = chain
                .request()
                .newBuilder()
                .addHeader("content_type","Content-Type: application/json")
                .build()
            chain.proceed(request)
        }

    val provideGson = GsonConverterFactory.create()

    fun provideClient(interceptor: Interceptor, context: Context) = OkHttpClient.Builder()
        .readTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()

    fun provideRetrofit(gson : GsonConverterFactory, client : OkHttpClient) : Retrofit =  Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(gson)
            .client(client)
            .build()

    single { provideInterceptor }
    single { provideGson }
    single { provideClient(get(), androidContext()) }
    single { provideRetrofit(get(), get()) }
}
val repositoryModule = module {
    single { AmbulanceRepository(get()) } bind IAmbulanceRepository::class
}

val viewModelModule = module {
    viewModel { MapsViewModel() }
}

val dispatcherModule = module {
    single {
        Dispatchers.IO
    }
}