package com.example.ambulanceondemand.ui.landing.ambulanceresponses

data class Ambulance(
    val distance: Int?,
    val distanceOnTheRoad: DistanceOnTheRoad?,
    val duration: Duration?,
    val geopoint: Geopoint?,
    val id: String?,
    val kontakPicAmbulance: String?,
    val namaDriver: String?,
    val namaInstansi: String?,
    val platNomor: String?
)