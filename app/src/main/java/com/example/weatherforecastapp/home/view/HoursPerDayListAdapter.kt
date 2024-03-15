package com.example.weatherforecastapp.home.view
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.model.WeatherData
import com.example.weatherforecastapp.utilities.getSeparateDataAndTime
import com.google.android.material.textview.MaterialTextView


class HoursPerDayListAdapter:ListAdapter<WeatherData,HoursPerDayListAdapter.HoursPerDayViewHolder>(WeatherDataDiffUtil()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursPerDayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_per_hour_item,parent,false)
        return HoursPerDayViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HoursPerDayViewHolder, position: Int) {
        val currentHoursPerDay:WeatherData = getItem(position)
        val dateAndTime:String=currentHoursPerDay.dt_txt
        val separateDateAndTime = getSeparateDataAndTime(dateAndTime)
        val time = separateDateAndTime.toLocalTime()
        holder.time.text= time.toString()

        Glide.with(holder.itemView.context)
            .load("https://openweathermap.org/img/wn/"+currentHoursPerDay.weather[position].icon+".png")
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.weatherStatus)

        holder.temp.text = currentHoursPerDay.main.temp_kf.toString()

    }

    class HoursPerDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val weatherStatus: ImageFilterView = itemView.findViewById(R.id.hourlyStatusImageView)
        val temp:MaterialTextView=itemView.findViewById(R.id.hourlyTempTextView)
        val time:MaterialTextView=itemView.findViewById(R.id.hourTimeTextView)

    }

/*
https://openweathermap.org/img/wn/10d.png
 */
}
