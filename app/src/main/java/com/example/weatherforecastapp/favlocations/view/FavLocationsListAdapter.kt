package com.example.weatherforecastapp.favlocations.view



import android.os.Build
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.model.LocationData
import com.google.android.material.card.MaterialCardView

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView

 class FavLocationsListAdapter(private val favLocationlistener: OnFavLocationClickListener, private val deleteLocationListener: OnRemoveLocationClickListener) : ListAdapter<LocationData, FavLocationsListAdapter.viewHolder>(FavLocationsDataDiffUtil()) {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_location_item,parent,false)
        return viewHolder(view)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentLocation= getItem(position)
        holder.locationTitle.text = currentLocation.cityName
        holder.favLocationCardView.setOnClickListener{
            favLocationlistener.OnFavLocationClick(currentLocation)
        }
        holder.btnRemove.setOnClickListener{

            deleteLocationListener.onRemoveLocation(currentLocation)
        }



    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val locationTitle :MaterialTextView = itemView.findViewById(R.id.locationTitleTextView)

        val btnRemove: FloatingActionButton= itemView.findViewById(R.id.btnRemove)

        val favLocationCardView:MaterialCardView= itemView.findViewById(R.id.favLocaionCradView)

    }

}


