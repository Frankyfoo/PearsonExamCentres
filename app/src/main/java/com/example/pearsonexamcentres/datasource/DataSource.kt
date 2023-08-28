package com.example.pearsonexamcentres.datasource

import com.example.pearsonexamcentres.R
import com.example.pearsonexamcentres.model.ExamCentreModel
import com.google.android.gms.maps.model.LatLng

class DataSource {
    fun loadExamCentres(): List<ExamCentreModel> {
        return listOf(
            ExamCentreModel(
                R.string.center_title3,
                R.drawable.exam_image_3,
                "09:00 - 17:00",
                LatLng(3.1570573,101.7097439),
                "One of Pearson VUE certified exam locations",
                "25th Floor, Menara TA One, 22 Jalan P.Ramlee, 50250 Kuala Lumpur, Malaysia",
                " 603-21621088"
            ),
            ExamCentreModel(
                R.string.center_title1,
                R.drawable.exam_image_1,
                "09:00 - 17:00",
                LatLng(3.1322685,101.7128994),
                "One of Pearson VUE certified exam locations",
                "Level 5, Wisma Naza, No. 12, Jalan Sungei Besi, 57100 Kuala Lumpur",
                "603-20210517"
            ),
            ExamCentreModel(
                R.string.center_title2,
                R.drawable.exam_image_2,
                "09:00 - 17:00",
                LatLng(3.1535382,101.6956672),
                "One of Pearson VUE certified exam locations",
                "6th Floor, KWSP Building, No.3, Changkat Raja Chulan, Off Jalan Raja Chulan, 50200 Kuala Lumpur, Malaysia",
                "603-20503610"
            ),

        )
    }
}