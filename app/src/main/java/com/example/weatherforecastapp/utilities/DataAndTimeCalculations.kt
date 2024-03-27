package com.example.weatherforecastapp.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherforecastapp.model.WeatherData
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
fun getAllDatesOfTheCurrentDate(weatherDataList: List<WeatherData>): List<WeatherData> {


    val currentDate = LocalDate.now()

    return weatherDataList
        .filter { weatherData ->
            val dateTime = LocalDateTime.parse(weatherData.dt_txt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            val instant = dateTime.atZone(ZoneId.systemDefault()).toInstant()

            val date = instant.atZone(ZoneId.systemDefault()).toLocalDate()

            date == currentDate
        }

}


@RequiresApi(Build.VERSION_CODES.O)
fun getAllDatesExceptCurrentDate(weatherDataList: List<WeatherData>): List<WeatherData> {
    val currentDate = LocalDate.now()

    val groupedByDate = weatherDataList.groupBy { weatherData ->
        Instant.ofEpochSecond(weatherData.dt).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    return groupedByDate
        .filter { (date, _) -> date != currentDate }
        .flatMap { (_, items) -> items.minByOrNull { it.dt }?.let { listOf(it) } ?: emptyList() }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getSeparateDataAndTime(dateAndTime: String): Pair<LocalDateTime, DayOfWeek> {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formattedDateAndTime = LocalDateTime.parse(dateAndTime, formatter)
    val dayOfWeek = formattedDateAndTime.dayOfWeek
    return Pair(formattedDateAndTime, dayOfWeek)


}


fun metersPerSecondToMilesPerHour(mps: Double): Double {
    return mps * 2.23694
}



fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(calendar.time)
}








