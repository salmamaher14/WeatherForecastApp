package com.example.weatherforecastapp.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.model.WeatherRepository
import com.example.weatherforecastapp.utilities.ForecastWeatherState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val _irepo:WeatherRepository):ViewModel() {
    private val _weatherResponseState= MutableStateFlow<ForecastWeatherState>(ForecastWeatherState.Loading)
    val weatherResponseState: StateFlow<ForecastWeatherState> = _weatherResponseState

    init{
        getRemoteForecastWeather(0.0,0.0)
    }

    fun getRemoteForecastWeather(latitude:Double,longitude:Double){
        // coroutines
        viewModelScope.launch(Dispatchers.IO){

            _irepo.getForecastWeather(latitude,longitude)
                .catch { e->
                    _weatherResponseState.value= ForecastWeatherState.Failure(e) //post value to view

                }
                .collect{
                    weatherResponse->_weatherResponseState.value=ForecastWeatherState.Success(weatherResponse)
                }
        }
    }





}

/*
 fun getRemoteProducts(){
         viewModelScope.launch (Dispatchers.IO ){
             _irepo.getAllProducts()
                 .catch { e->
                     _productState.value=ApiState.Failure(e)
                 }
                 .collect{
                     productsList->
                     _productState.value=ApiState.Success(productsList)
                 }
         }
    }
 */

/*
class AllProductsViewModel(private val _irepo:ProductsRepository):ViewModel() {




    init{
        getRemoteProducts()
    }

     fun getRemoteProducts(){
         viewModelScope.launch (Dispatchers.IO ){
             _irepo.getAllProducts()
                 .catch { e->
                     _productState.value=ApiState.Failure(e)
                 }
                 .collect{
                     productsList->
                     _productState.value=ApiState.Success(productsList)
                 }
         }
    }



    fun insertProduct(product: Product){
        viewModelScope.launch(Dispatchers.IO) {
            _irepo.insertProduct(product)

        }
    }

}

 */