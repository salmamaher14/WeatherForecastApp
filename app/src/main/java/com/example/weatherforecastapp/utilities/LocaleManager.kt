package com.example.weatherforecastapp.utilities

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import java.util.Locale

object LocaleManager {
    private const val TAG = "LocaleManager"

    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        context.resources.updateConfiguration(configuration, resources.displayMetrics)

        Log.d(TAG, "Locale set to: $languageCode")
    }
}
object DayNameMapper {
    fun mapEnglishToArabic(englishDayName: String): String {
        return when (englishDayName.toLowerCase()) {
            "sunday" -> "الأحد"
            "monday" -> "الاثنين"
            "tuesday" -> "الثلاثاء"
            "wednesday" -> "الأربعاء"
            "thursday" -> "الخميس"
            "friday" -> "الجمعة"
            "saturday" -> "السبت"
            else -> englishDayName // If the day name is not recognized, return the original string
        }
    }
}

