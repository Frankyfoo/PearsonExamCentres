package com.example.pearsonexamcentres.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pearsonexamcentres.LocationDetailsActivity
import com.example.pearsonexamcentres.R
import com.example.pearsonexamcentres.model.ExamCentreModel

class ExamCentreAdapter(var context: Context, private val examCentres: List<ExamCentreModel>):
    RecyclerView.Adapter<ExamCentreAdapter.ExamCentreViewHolder>() {

    class ExamCentreViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val examCentreTitle: TextView = itemView.findViewById(R.id.examCentreTitle)
        val examCardView: CardView = itemView.findViewById(R.id.examCardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamCentreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exam_centre_card, parent, false)
        return ExamCentreViewHolder(view)
    }

    override fun getItemCount(): Int {
        return examCentres.size
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ExamCentreViewHolder, position: Int) {
        val examCentre = examCentres[position]

        holder.examCentreTitle.text = context.getString(examCentre.examCentreTitle)
        Glide.with(holder.itemView.context)
            .load(context.getDrawable(examCentre.imageResourceId))
            .into(holder.imageView)

        holder.examCardView.setOnClickListener {
            val intent = Intent(context, LocationDetailsActivity::class.java)
            val bundle = Bundle()
            bundle.putString("CentreName", context.getString(examCentre.examCentreTitle))
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

}