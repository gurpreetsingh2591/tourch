package com.tourch.ui.rider.model

import com.google.gson.annotations.SerializedName

data class DropOff(
    @SerializedName("latitude")
    var latitude: Double? = 0.0,
    @SerializedName("longitude")
    var longitude: Double? = 0.0,
    @SerializedName("location")
    var location: String? = "",
    @SerializedName("city")
    var city: String? = ""
)