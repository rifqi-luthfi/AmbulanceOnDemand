package com.example.ambulanceondemand.util

import android.content.Context
import android.content.SharedPreferences

class SharedPrefUtil(private val context : Context) {

    private var sharedPreferences : SharedPreferences
    private var sharedPreferencesRegistration : SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences("AmbulanceTools", Context.MODE_PRIVATE)
        sharedPreferencesRegistration = context.getSharedPreferences("AmbulanceToolsRegistration", Context.MODE_PRIVATE)
    }

}