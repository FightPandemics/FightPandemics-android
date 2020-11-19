package com.fightpandemics.filter.ui

import android.os.Parcel
import android.os.Parcelable

// Data class for making a filter request
data class FilterRequest(
    val location: String?,
    val latitude: Double?,
    val longitude: Double?,
    val fromWhomFilters: List<String>?,
    val typeFilters: List<String>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.createStringArrayList(),
        parcel.createStringArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(location)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeStringList(fromWhomFilters)
        parcel.writeStringList(typeFilters)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FilterRequest> {
        override fun createFromParcel(parcel: Parcel): FilterRequest {
            return FilterRequest(parcel)
        }

        override fun newArray(size: Int): Array<FilterRequest?> {
            return arrayOfNulls(size)
        }
    }
}
