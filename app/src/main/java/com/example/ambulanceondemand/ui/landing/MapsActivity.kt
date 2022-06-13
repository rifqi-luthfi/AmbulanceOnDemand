package com.example.ambulanceondemand.ui.landing

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import com.example.ambulanceondemand.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.ambulanceondemand.databinding.ActivityMapsBinding
import com.example.ambulanceondemand.ui.verification.VerificationPage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.maps.android.PolyUtil
import com.example.ambulanceondemand.ui.landing.model.DirectionResponses
import com.google.android.gms.maps.model.*
import id.byu.salesagen.external.extension.GONE
import id.byu.salesagen.external.extension.VISIBLE
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var destinationHospital: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setDataDriver()

        binding.tvCallAmbulance.setOnClickListener {
            val intentVerification = Intent(this, VerificationPage::class.java)
            startActivity(intentVerification)
            finish()
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setupViewModel() {
        mapsViewModel = ViewModelProvider(this)[MapsViewModel::class.java]
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getMyLastLocation()

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    showStartMarker(location)
                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showStartMarker(location: Location) {

        val startLocation = LatLng(location.latitude, location.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .title("Lokasi Jemput")
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 14f))
        Log.d("Debug:" ,"Your Location:"+ location.longitude)

        binding.actvPickUpLocation.text = getCityName(location.latitude,location.longitude)

        val locationPoint = location.latitude.toString() + "," + location.longitude.toString()
        mapsViewModel.setDestination(locationPoint, 5000, "hospital" , "AIzaSyC3RwBupXyFdul5XtIAWjDsF9f8ogyLam4")


        mapsViewModel.getDestination.observe(this) { destination ->
            val destinastionLat = destination.results?.get(0)?.geometry?.location?.lat
            val destinastionLong = destination.results?.get(0)?.geometry?.location?.lng
            destinationHospital = LatLng(destinastionLat!!, destinastionLong!!)
            val destinationMarker = MarkerOptions()
                .position(destinationHospital)
                .title("Lokasi Antar")
                .icon(vectorToBitmap(R.drawable.ic_destination, Color.parseColor("#AE0505")))

            mMap.addMarker(destinationMarker)

            val locationDestination = destinastionLat.toString() + "," + destinastionLong.toString()
            mapsViewModel.setRoute(locationPoint, locationDestination, "AIzaSyC3RwBupXyFdul5XtIAWjDsF9f8ogyLam4")

            binding.actvDropLocation.text = destination.results.get(0)?.name

        }

        mapsViewModel.getRoute.observe(this, { route ->
            drawPolyline(route)
            durationRoute(route)
        })

    }

    private fun setDataDriver() {
        val name = intent.getStringExtra(EXTRA_DRIVER_NAME)
        val number = intent.getStringExtra(EXTRA_AMBULANCE_NUMBER)
        val pic = intent.getStringExtra(EXTRA_CONTACT_PIC_NUMBER)
        val hospital = intent.getStringExtra(EXTRA_HOSPITAL_NAME)
        val latDriver = intent.getStringExtra(EXTRA_DRIVER_LAT)
        val lngDriver = intent.getStringExtra(EXTRA_DRIVER_LNG)
//        val latLngDriver = LatLng(latDriver, lngDriver)

        if (name.isNullOrEmpty()){
            binding.clContainerBottom2.visibility = GONE
            binding.clContainerBottom1.visibility = VISIBLE
        }else{
            binding.apply {
                clContainerBottom2.visibility = VISIBLE
                clContainerBottom1.visibility = GONE
                tvNameDriverAmbulance.text = name
                tvDestinationHospital.text = hospital
                tvNumberDriverAmbulance.text = number

//                val driverMarker = MarkerOptions()
//                    .position(latLngDriver)
//                    .title("Lokasi Pengemudi")
//                    .icon(vectorToBitmap(R.drawable.ic_ambulance, Color.parseColor("#393996")))
//
//                mMap.addMarker(driverMarker)
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(driverMarker, 15f))

            }
        }
    }

    private fun getCityName(lat: Double,long: Double):String{
        val geoCoder = Geocoder(this, Locale.getDefault())
        val Adress = geoCoder.getFromLocation(lat,long,5)

        val street = Adress.get(0).thoroughfare
        val numStreet = Adress.get(0).subThoroughfare
        val kecamatan = Adress.get(0).locality
        val kota = Adress.get(0).adminArea
        val countryName = Adress.get(0).countryName
        val cityName = "$street $numStreet, $kecamatan, $kota"
        Log.d("Lokasi sekarang","Your street: " + street + " ; Your City: " + cityName + " ; your Country " + countryName)
        return cityName

        /**
         * sublocality = kelurahan
         * locality = kecamatan
         * countryName = negara
         * thoroughfare = nama jalan
         * subthoroughfare = nomor rumah
         */
    }

    private fun drawPolyline(response : DirectionResponses) {
        val shape = response.routes?.get(0)?.overviewPolyline?.points
        val polyline = PolylineOptions()
            .addAll(PolyUtil.decode(shape))
            .width(8f)
            .color(Color.RED)
        mMap.addPolyline(polyline)
    }

    private fun durationRoute(response : DirectionResponses) {
        val duration = response.routes?.get(0)?.legs?.get(0)?.duration
        val time = " ${duration?.text}"
        binding.tvTimePickerLocation.text = time
    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
        val bitmap = Bitmap.createBitmap(
            vectorDrawable!!.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {
        const val EXTRA_DRIVER_NAME = "extra driver name"
        const val EXTRA_AMBULANCE_NUMBER = "extra ambulance number"
        const val EXTRA_CONTACT_PIC_NUMBER = "extra contact pic number"
        const val EXTRA_HOSPITAL_NAME = "extra hospital name"
        const val EXTRA_DRIVER_LAT = "extra driver lat"
        const val EXTRA_DRIVER_LNG = "extra driver lng"
    }

}