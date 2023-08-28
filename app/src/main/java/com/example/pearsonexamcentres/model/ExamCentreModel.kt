package com.example.pearsonexamcentres.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.gms.maps.model.LatLng

data class ExamCentreModel(
    @StringRes val examCentreTitle: Int,
    @DrawableRes val imageResourceId: Int,
    val operatingHours: String,
    val location: LatLng,
    val description: String,
    val address: String,
    val phoneNumber: String
)