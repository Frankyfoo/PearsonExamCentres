package com.example.pearsonexamcentres

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.pearsonexamcentres.datasource.DataSource
import com.example.pearsonexamcentres.model.ExamCentreModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var examCentreList: List<ExamCentreModel> = DataSource().loadExamCentres()
    private lateinit var examCentre: ExamCentreModel
    private lateinit var examLocation: LatLng
    private var userLocation: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hide action bar / app bar (only for this screen)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_maps)

        // get the name of the Exam Centre selected
        val bundle = intent.extras
        val centreName = bundle?.getString("CentreName")

        // get only the exam centre that matches the exam centre name
        examCentre = examCentreList.firstOrNull { this.getString(it.examCentreTitle) == centreName }!!

        // get exam centre location
        if(examCentre != null) {
            examLocation = LatLng(examCentre.location.latitude, examCentre.location.longitude)
        }

        getUserLocation()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getUserLocation() {
        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
        }
        val lastLocation = fusedLocationClient.lastLocation

        lastLocation.addOnSuccessListener {
            if (it != null) {
                userLocation = LatLng(it.latitude, it.longitude)
            }
            else {
                userLocation = null
                Toast.makeText(this, "Could not find your location.",Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Showing the exam centre location instead.",Toast.LENGTH_SHORT).show()
            }
        }

        // if not given access, display toast of warning
        lastLocation.addOnFailureListener {
            Toast.makeText(this, "Access To Location has been denied.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap;
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL;
        // if userLocation available, show location of user and exam centre location
        if(examLocation != null) {
            // if userLocation unavailable, show only exam centre location
            if (userLocation != null) {
                val markerLocation = MarkerOptions().position(examLocation).title(this.getString(examCentre.examCentreTitle)).snippet(examCentre.address)
                val userCurrentLocation = MarkerOptions().position(userLocation!!).title("Your location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))

                // add marker to map
                mMap.addMarker(markerLocation)
                mMap.addMarker(userCurrentLocation)

                val bounds = LatLngBounds.Builder().include(examLocation).include(userLocation!!).build()

                // Set the map's camera to show the LatLngBounds
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 200)
                googleMap.moveCamera(cameraUpdate)
            }
            // if userLocation unavailable, show only exam centre location
            else {
                val markerLocation = MarkerOptions().position(examLocation).title(this.getString(examCentre.examCentreTitle)).snippet(examCentre.address)

                // add marker to map
                mMap.addMarker(markerLocation)

                // zoom to the marker
                val cameraPosition = CameraPosition.Builder()
                    .target(examLocation)
                    .zoom(15f)
                    .build()
                val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
                googleMap.animateCamera(cameraUpdate)
            }
        }
    }
}