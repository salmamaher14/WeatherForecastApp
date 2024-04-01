package com.example.weatherforecastapp.favlocations.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.favlocations.viewmodel.FavLocationsViewModel
import com.example.weatherforecastapp.favlocations.viewmodel.FavLocationsViewModelFactory
import com.example.weatherforecastapp.local.WeatherLocalDataSourceImpl
import com.example.weatherforecastapp.model.LocationData
import com.example.weatherforecastapp.network.WeatherRemoteDataSourceImpl
import com.example.weatherforecastapp.repo.WeatherRepositoryImpl
import com.example.weatherforecastapp.utilities.ForecastWeatherState
import com.example.weatherforecastapp.utilities.getAllDatesExceptCurrentDate
import com.example.weatherforecastapp.utilities.getAllDatesOfTheCurrentDate
import com.example.weatherforecastapp.utilities.getCurrentDate
import com.example.weatherforecastapp.utilities.getSeparateDataAndTime
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavLocationsFragment : Fragment(),OnFavLocationClickListener,OnRemoveLocationClickListener{

    private lateinit var favLocationsRv:RecyclerView
    private lateinit var favLocationsAdapter:FavLocationsListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var btnOpenMap:FloatingActionButton
    private lateinit var favViewModel: FavLocationsViewModel
    private lateinit var  favFactory: FavLocationsViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_locations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initFavFragmentParameters()
        setRecyclerViewParameters()

        btnOpenMap.setOnClickListener{
            findNavController().navigate(R.id.action_favLocationsFragment_to_mapFragment)

        }

//        favViewModel.favLocations.observe(
//            viewLifecycleOwner, object : Observer<List<LocationData>> {
//                override fun onChanged(locationsList: List<LocationData>) {
//                    favLocationsAdapter.submitList(locationsList)
//                }
//
//            }
//        )


        lifecycleScope.launch {
            favViewModel.favLocations.collect{
                    favLocationsList ->
                favLocationsAdapter.submitList(favLocationsList)

            }
        }

    }


    fun initUi(){

       favLocationsRv= requireView().findViewById(R.id.favLocationsRecyclerView)
        btnOpenMap= requireView().findViewById(R.id.btnAddAlert)

    }

    fun setRecyclerViewParameters(){
        layoutManager= LinearLayoutManager(context)
        favLocationsAdapter=FavLocationsListAdapter(this,this)
        layoutManager.orientation = RecyclerView.VERTICAL
        favLocationsRv.adapter = favLocationsAdapter

    }

    private fun initFavFragmentParameters(){

        favFactory =
            FavLocationsViewModelFactory(
                WeatherRepositoryImpl.getInstance(
                WeatherRemoteDataSourceImpl.getInstance(), WeatherLocalDataSourceImpl(requireContext())
                ))
        favViewModel = ViewModelProvider(requireActivity(), favFactory).get(FavLocationsViewModel::class.java)
    }

    override fun OnFavLocationClick(location: LocationData) {
        val action = FavLocationsFragmentDirections.actionFavLocationsFragmentToHomeFragment(location)
        findNavController().navigate(action)
    }

    override fun onRemoveLocation(location: LocationData) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Are you sure you want to delete this location?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // User clicked Yes, delete the location
                favViewModel.deleteLocation(location)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, id ->
                // User clicked No, do nothing
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.show()
    }





}
