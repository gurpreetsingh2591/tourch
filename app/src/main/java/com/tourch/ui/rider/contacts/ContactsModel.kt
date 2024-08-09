package com.tourch.ui.rider.contacts

import android.os.Parcel
import android.os.Parcelable

data class ContactsModel(
    val name: String?,
    val number: String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(number)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactsModel> {
        override fun createFromParcel(parcel: Parcel): ContactsModel {
            return ContactsModel(parcel)
        }

        override fun newArray(size: Int): Array<ContactsModel?> {
            return arrayOfNulls(size)
        }
    }
}