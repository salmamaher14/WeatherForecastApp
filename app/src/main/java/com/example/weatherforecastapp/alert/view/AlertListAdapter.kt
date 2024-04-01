package com.example.weatherforecastapp.alert.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R

import com.example.weatherforecastapp.model.WeatherAlert
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView

class AlertListAdapter(private val deleteAlertListener: OnRemoveAlertClickListener): ListAdapter<WeatherAlert, AlertListAdapter.viewHolder>(AlertDataDiffUtil()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alert_item,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentAlert= getItem(position)
        holder.alertDate.text = currentAlert.date

        holder.btnRemove.setOnClickListener{

            deleteAlertListener.OnRemoveAlertClick(currentAlert)
        }
    }


    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val alertDate : MaterialTextView = itemView.findViewById(R.id.alertTitleTextView)

        val btnRemove: FloatingActionButton = itemView.findViewById(R.id.btnRemoveAlert)

    }

}


