package com.example.ambulanceondemand.ui.landing.ambulanceresponses

data class AmbulanceResponse(
    val ambulances: List<Ambulance?>?,
    val found: Int?,
    val origin_addresses: List<String?>?
)