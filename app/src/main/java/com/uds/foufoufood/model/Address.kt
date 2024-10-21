package com.uds.foufoufood.model

import android.os.Parcelable

data class Address (
    var street: String? = null,
    var number: Number? = null,
    var city: String? = null,
    var state: String? = null,
    var zipCode: String? = null,
    var country: String? = null
) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
        parcel.readString(),
        parcel.readValue(Number::class.java.classLoader) as Number?,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(street)
        parcel.writeValue(number)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(zipCode)
        parcel.writeString(country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Address> {
        override fun createFromParcel(parcel: android.os.Parcel): Address {
            return Address(parcel)
        }

        override fun newArray(size: Int): Array<Address?> {
            return arrayOfNulls(size)
        }
    }
}