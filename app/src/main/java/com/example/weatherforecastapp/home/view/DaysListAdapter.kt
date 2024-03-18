package com.example.weatherforecastapp.home.view

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.model.WeatherData
import com.example.weatherforecastapp.utilities.getSeparateDataAndTime
import com.google.android.material.textview.MaterialTextView

class DaysListAdapter : ListAdapter<WeatherData, DaysListAdapter.DaysViewHolder>(WeatherDataDiffUtil()) {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): DaysViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_per_day,parent,false)
        return DaysViewHolder(view)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val currentday:WeatherData = getItem(position)

        val dateAndTime:String=currentday.dt_txt

        val separateDateAndTime = getSeparateDataAndTime(dateAndTime)

        holder.dayOfWeek.text=getSeparateDataAndTime(dateAndTime).second.name

        holder.weatherDescription.text=currentday.weather[0].description

        holder.temp.text=currentday.main.temp.toString()

        Glide.with(holder.itemView.context)
            .load("https://openweathermap.org/img/wn/${currentday.weather[0].icon}.png")
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.weatherStatus)



    }

    class DaysViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val dayOfWeek: MaterialTextView = itemView.findViewById(R.id.dayNameTextView)

        val weatherStatus:ImageFilterView=itemView.findViewById(R.id.weatherStateImageView)

        val weatherDescription:MaterialTextView=itemView.findViewById(R.id.weatherDescTextView)

        val temp:MaterialTextView=itemView.findViewById(R.id.tempDegreeTextView)


    }




}


