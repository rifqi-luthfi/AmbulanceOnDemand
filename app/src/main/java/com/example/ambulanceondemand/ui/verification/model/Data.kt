package com.example.ambulanceondemand.ui.verification.model

data class Data(
    val alamatDaerahOperasiAmbulance: String?,
    val geopoint: Geopoint?,
    val id: String?,
    val keterangan: String?,
    val kontakPicAmbulance: String?,
    val kotaKabupaten: String?,
    val namaDriver: String?,
    val namaInstansi: String?,
    val platNomor: String?,
    val provinsi: String?
)