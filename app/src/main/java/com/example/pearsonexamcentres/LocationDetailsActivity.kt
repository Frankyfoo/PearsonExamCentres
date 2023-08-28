package com.example.pearsonexamcentres

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pearsonexamcentres.datasource.DataSource
import com.example.pearsonexamcentres.model.ExamCentreModel

class LocationDetailsActivity : AppCompatActivity() {

    private var examCentreList: List<ExamCentreModel> = DataSource().loadExamCentres()
    private lateinit var imageView: ImageView
    private lateinit var centreNameTitle: TextView
    private lateinit var operatingHours: TextView
    private lateinit var description: TextView
    private lateinit var address: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var buttonFindLocation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)

        imageView = findViewById(R.id.selectedImage)
        centreNameTitle = findViewById(R.id.tvExamCentreTitle)
        operatingHours = findViewById(R.id.tvOperatingHours)
        description = findViewById(R.id.tvCentreDescription)
        address = findViewById(R.id.tvAddress)
        phoneNumber = findViewById(R.id.tvPhoneNumber)
        buttonFindLocation = findViewById(R.id.btnFindLocation)

        // get ExamCentreName
        val bundle = intent.extras
        val centreName = bundle?.getString("CentreName")

        val examCentre = examCentreList.firstOrNull { this.getString(it.examCentreTitle) == centreName }

        if (examCentre != null) {
            loadDetails(examCentre)

            buttonFindLocation.setOnClickListener {
                val intent = Intent(this, MapsActivity::class.java)
                val bundle = Bundle()
                bundle.putString("CentreName", this.getString(examCentre.examCentreTitle))
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    private fun loadDetails(examCentre: ExamCentreModel) {
        if (examCentre != null) {
            Glide.with(this)
                .load(this.getDrawable(examCentre.imageResourceId))
                .into(imageView)
            centreNameTitle.text = this.getString(examCentre.examCentreTitle)
            operatingHours.text = "Operating Hours: ${examCentre.operatingHours}"
            description.text = "Description: ${examCentre.description}"
            address.text = "Address: ${examCentre.address}"
            phoneNumber.text = "Phone Number: ${examCentre.phoneNumber}"
        }
    }
}