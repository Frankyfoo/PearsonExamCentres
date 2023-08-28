package com.example.pearsonexamcentres

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pearsonexamcentres.adapter.ExamCentreAdapter
import com.example.pearsonexamcentres.datasource.DataSource
import com.example.pearsonexamcentres.model.ExamCentreModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {

    private var examCentreList: MutableList<ExamCentreModel> = DataSource().loadExamCentres() as MutableList<ExamCentreModel>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnSearchNearest: Button
    private lateinit var examAdapter: ExamCentreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ask permission for accessing location when opening app for first time
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
        }

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnSearchNearest = findViewById(R.id.btnSearch)

        recyclerView = findViewById(R.id.recyclerView)
        examAdapter = ExamCentreAdapter(this, examCentreList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = examAdapter

        btnSearchNearest.setOnClickListener {
            getNearestLocation()
        }
    }

    private fun getNearestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
        }
        val lastLocation = fusedLocationClient.lastLocation

//        // testing
//        val userLocation = LatLng(3.0561342, 101.6958775)

        lastLocation.addOnSuccessListener {
            if( it != null) {
                val userLocation = LatLng(it.latitude, it.longitude)

//                // test (current location)
//                val userLocation = LatLng(3.0561342, 101.6958775)

//                // test (location is near scicom)
//                val userLocation = LatLng(3.158752,101.7074914)
//
//                // test (location is near Olympia Building)
//                val userLocation = LatLng(3.153143,101.6926543)

                // sorts to the nearest location
                examCentreList.sortBy { examCentre ->
                    val distance = FloatArray(1)
                    Location.distanceBetween(userLocation.latitude, userLocation.longitude,
                        examCentre.location.latitude, examCentre.location.longitude, distance)
                    distance[0]
                }

                examAdapter.notifyDataSetChanged()

                Toast.makeText(this, "Nearest: ${this.getString(examCentreList[0].examCentreTitle)}.",Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Could not your location.",Toast.LENGTH_SHORT).show()
            }
        }

        // if not given access, display toast of warning
        lastLocation.addOnFailureListener {
            Toast.makeText(this, "Access To Location has been denied.", Toast.LENGTH_SHORT).show()
        }
    }
}