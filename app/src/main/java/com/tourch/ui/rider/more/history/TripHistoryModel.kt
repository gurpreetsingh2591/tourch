package com.tourch.ui.rider.more.history

import com.google.gson.annotations.SerializedName
import com.tourch.ui.rider.model.TripDetailsApiResponse

data class TripHistoryModel(
    @SerializedName("pick_up") var pickUp: TripDetailsApiResponse.PickUp? = TripDetailsApiResponse.PickUp(),
    @SerializedName("trip_id") var tripId: String? = null,
    @SerializedName("total_distance") var totalDistance: Int? = null,
    @SerializedName("formatted_total_distance") var formattedTotalDistance: String? = null,
    @SerializedName("drop_off") var dropOff: ArrayList<TripDetailsApiResponse.DropOff> = arrayListOf(),
    @SerializedName("passenger_to_driver_ratings") var passengerToDriverRatings: Int? = null,
    @SerializedName("passenger_to_driver_experience") var passengerToDriverExperience: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("service_info") var serviceInfo: TripDetailsApiResponse.ServiceInfo? = TripDetailsApiResponse.ServiceInfo(),
    @SerializedName("fare_info") var fareInfo: TripDetailsApiResponse.FareInfo? = TripDetailsApiResponse.FareInfo(),
    @SerializedName("booking_date") var bookingDate: String? = null,
    @SerializedName("driver_info") var driverInfo: TripDetailsApiResponse.DriverInfo? = TripDetailsApiResponse.DriverInfo(),
    @SerializedName("vehicle_info") var vehicleInfo: TripDetailsApiResponse.VehicleInfo? = TripDetailsApiResponse.VehicleInfo()
)