package com.tourch.ui.rider.model


import com.google.gson.annotations.SerializedName

class ServiceListApiResponse : ArrayList<ServiceListApiResponse.ServiceListApiResponseItem>(){
    data class ServiceListApiResponseItem(
        @SerializedName("code")
        var code: String? = "",
        @SerializedName("icon")
        var icon: String? = "",
        @SerializedName("service_id")
        var serviceId: String? = "",
        @SerializedName("title")
        var title: String? = "",
        var isSelected : Boolean = false
    )
}