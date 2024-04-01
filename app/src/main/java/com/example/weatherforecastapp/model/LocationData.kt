package com.example.weatherforecastapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_locations")
data class LocationData(
    @PrimaryKey val cityName: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cityName)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationData> {
        override fun createFromParcel(parcel: Parcel): LocationData {
            return LocationData(parcel)
        }

        override fun newArray(size: Int): Array<LocationData?> {
            return arrayOfNulls(size)
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as LocationData

        if (cityName != that.cityName) return false
        if (latitude != that.latitude) return false
        if (longitude != that.longitude) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cityName.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }
}
