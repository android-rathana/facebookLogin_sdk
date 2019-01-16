package org.khmeracademy.util.helper.facebook

import android.os.Parcel
import android.os.Parcelable

data class FacebookUser(
        val accessToken: String,
        val userId: String,
        val firstName: String,
        val lastName: String,
        val phone: String,
        val dateOfBirth: String,
        val email : String,
        val pictureProfile: String,
        val gender: String,
        val link : String
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accessToken)
        parcel.writeString(userId)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(phone)
        parcel.writeString(dateOfBirth)
        parcel.writeString(email)
        parcel.writeString(pictureProfile)
        parcel.writeString(gender)
        parcel.writeString(link)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FacebookUser> {
        override fun createFromParcel(parcel: Parcel): FacebookUser {
            return FacebookUser(parcel)
        }

        override fun newArray(size: Int): Array<FacebookUser?> {
            return arrayOfNulls(size)
        }
    }
}