package com.tourch.ui.rider.model

import com.google.gson.annotations.SerializedName

data class EstimateFareApiResponse(
    @SerializedName("total_distance") var totalDistance: Double? = null,
    @SerializedName("formatted_total_distance") var formattedTotalDistance: String? = null,
    @SerializedName("total_duration") var totalDuration: Int? = null,
    @SerializedName("formatted_total_duration") var formattedTotalDuration: String? = null,
    @SerializedName("rides") var rides: ArrayList<Ride> = arrayListOf()
) {
    data class Ride(
        @SerializedName("vehicle_ride_option_id") var vehicleRideOptionId: String? = null,
        @SerializedName("icon") var icon: String? = null,
        @SerializedName("min_seats") var minSeats: Int? = null,
        @SerializedName("max_seats") var maxSeats: Int? = null,
        @SerializedName("type") var type: String? = null,
        @SerializedName("title") var title: String? = null,
        @SerializedName("caption") var caption: String? = null,
        @SerializedName("vehicle_duration") var vehicleDuration: VehicleDuration? = VehicleDuration(),
        @SerializedName("fare_info") var fareInfo: FareInfo? = FareInfo(),
        @SerializedName("total") var total: Double? = null,
        @SerializedName("fare_receipt") var fareReceipt: ArrayList<FareReceipt> = arrayListOf(),
        var isSelected: Boolean = false
    ) {
        data class FareInfo(
            @SerializedName("base_fare") var baseFare: Double? = null,
            @SerializedName("technology_fee") var technologyFee: Double? = null,
            @SerializedName("cost_per_mile") var costPerMile: Double? = null,
            @SerializedName("cost_per_min") var costPerMin: Double? = null,
            @SerializedName("min_base_mile") var minBaseMile: Double? = null,
            @SerializedName("min_base_fare") var minBaseFare: Double? = null,
            @SerializedName("wait_fees") var waitFees: Double? = null,
            @SerializedName("wait_fees_per_min") var waitFeesPerMin: Double? = null,
            @SerializedName("cancellation_fee") var cancellationFee: Double? = null,
            @SerializedName("incentives_multiplier") var incentivesMultiplier: Double? = null,
            @SerializedName("subtotal") var subtotal: Double? = null,
            @SerializedName("total") var total: Double? = null,
            @SerializedName("total_mile_fare") var totalMileFare: Double? = null,
            @SerializedName("total_min_fare") var totalMinFare: Double? = null,
            @SerializedName("is_min_fare") var isMinFare: Boolean? = null,
            @SerializedName("tax") var tax: Double? = null
        )

        data class FareReceipt(
            @SerializedName("label") var label: String? = null,
            @SerializedName("key") var key: String? = null,
            @SerializedName("value") var value: Double? = null
        )

        data class VehicleDuration(
            @SerializedName("duration_value") var durationValue: Int? = null,
            @SerializedName("duration_time") var durationTime: String? = null
        )
    }
}