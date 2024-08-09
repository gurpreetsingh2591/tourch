package com.tourch.ui.rider.model


import com.google.gson.annotations.SerializedName

class CancelReasonListApiResponse : ArrayList<CancelReasonListApiResponse.CancelReasonListApiResponseItem>(){
    data class CancelReasonListApiResponseItem(
        @SerializedName("cancel_ride_reason_criteria_id")
        var cancelRideReasonCriteriaId: String? = "",
        @SerializedName("title")
        var title: String? = "",
        var isSelected : Boolean = false
    )
}