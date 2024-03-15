package com.example.weatherforecastapp.utilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherforecastapp.model.WeatherData
import java.time.LocalDate
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun getAllDatesOfTheCurrentDate(weatherDataList:List<WeatherData>):List<WeatherData> {

    val currentDate = LocalDate.now()

    return weatherDataList
        .filter {weatherData ->
            val date= Instant.ofEpochSecond(weatherData.dt).atZone(ZoneId.systemDefault()).toLocalDate()
            date==currentDate
        }

}

@RequiresApi(Build.VERSION_CODES.O)
fun getSeparateDataAndTime(dateAndTime:String):LocalDateTime{

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formattedDateAndTime = LocalDateTime.parse(dateAndTime, formatter)

    return formattedDateAndTime

}



/*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main() {
    val inputDateTime = "2022-03-15 15:00:00"
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.parse(inputDateTime, formatter)

    val date = dateTime.toLocalDate()
    val time = dateTime.toLocalTime()

   val timeFormatted = time.format(DateTimeFormatter.ofPattern("h:mm a"))

    println("Date: $date")
    println("Time: $timeFormatted")
}
 */




