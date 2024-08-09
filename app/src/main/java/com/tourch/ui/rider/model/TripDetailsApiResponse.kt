package com.tourch.ui.rider.model


import com.google.gson.annotations.SerializedName

data class TripDetailsApiResponse(
    @SerializedName("book_by")
    var bookBy: String? = "",
    @SerializedName("book_for_someone_else_info")
    var bookForSomeoneElseInfo: String? = "",
    @SerializedName("booking_date")
    var bookingDate: String? = "",
    @SerializedName("driver_info")
    var driverInfo: DriverInfo? = DriverInfo(),
    @SerializedName("drop_off")
    var dropOff: ArrayList<DropOff> = arrayListOf(),
    @SerializedName("formatted_total_distance")
    var formattedTotalDistance: String? = "",
    @SerializedName("formatted_total_duration")
    var formattedTotalDuration: String? = "",
    @SerializedName("google_map_image")
    var googleMapImage: String? = "",
    @SerializedName("invoice_no")
    var invoiceNo: String? = "",
    @SerializedName("notes")
    var notes: String? = "",
    @SerializedName("number_of_travellers")
    var numberOfTravellers: Int? = 0,
    @SerializedName("passenger_info")
    var passengerInfo: PassengerInfo? = PassengerInfo(),
    @SerializedName("pick_up")
    var pickUp: PickUp? = PickUp(),
    @SerializedName("service_title")
    var serviceTitle: String? = "",
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("total")
    var total: Double? = 0.0,
    @SerializedName("trip_id")
    var tripId: String? = "",
    @SerializedName("vehicle")
    var vehicle: Vehicle? = Vehicle(),
    @SerializedName("vehicle_type_title")
    var vehicleTypeTitle: String? = "",
    @SerializedName("additional_charges")
    var additionalCharges: List<Any?>? = listOf(),
    @SerializedName("contribution")
    var contribution: Double? = 0.0,
    @SerializedName("discount")
    var discount: Double? = 0.0,
    @SerializedName("fare_info")
    var fareInfo: FareInfo? = FareInfo(),
    @SerializedName("is_favourite")
    var isFavourite: Boolean? = false,
    @SerializedName("is_flash")
    var isFlash: Boolean? = false,
    @SerializedName("outstation_trip_type")
    var outstationTripType: String? = "",
    @SerializedName("payment_type")
    var paymentType: String? = "",
    @SerializedName("requested_type")
    var requestedType: String? = "",
    @SerializedName("service_info")
    var serviceInfo: ServiceInfo? = ServiceInfo(),
    @SerializedName("subtotal")
    var subtotal: Double? = 0.0,
    @SerializedName("total_distance")
    var totalDistance: Double? = 0.0,
    @SerializedName("total_duration")
    var totalDuration: Double? = 0.0,
    @SerializedName("total_waiting_seconds")
    var totalWaitingSeconds: Double? = 0.0,
    @SerializedName("type")
    var type: String? = "",
    @SerializedName("vehicle_info")
    var vehicleInfo: VehicleInfo? = VehicleInfo(),
    @SerializedName("card_info")
    var cardInfo: String? = "",//CardInfo? = CardInfo(),
    @SerializedName("vehicle_ride_option_info")
    var vehicleRideOptionInfo: VehicleRideOptionInfo? = VehicleRideOptionInfo()
) {
    data class CardInfo(
        @SerializedName("id")
        var id: String? = "",
        @SerializedName("brand")
        var brand: String? = "",
        @SerializedName("exp_month")
        var exp_month: Int? = 0,
        @SerializedName("exp_year")
        var exp_year: Int? = 0,
        @SerializedName("last4")
        var last4: String? = ""
    ){}

    data class DriverInfo(
        @SerializedName("average_ratings")
        var averageRatings: Float? = 0F,
        @SerializedName("device_details")
        var deviceDetails: DeviceDetails? = DeviceDetails(),
        @SerializedName("email")
        var email: String? = "",
        @SerializedName("first_name")
        var firstName: String? = "",
        @SerializedName("is_favourite")
        var isFavourite: Boolean?= false,
        @SerializedName("language_code")
        var languageCode: String? = "",
        @SerializedName("last_name")
        var lastName: String? = "",
        @SerializedName("mobile")
        var mobile: String? = "",
        @SerializedName("notifications")
        var notifications: String? = "",
        @SerializedName("photo")
        var photo: String? = "",
        @SerializedName("user_id")
        var userId: String? = ""
    ) {
        data class DeviceDetails(
            @SerializedName("app_version")
            var appVersion: String? = "",
            @SerializedName("device_name")
            var deviceName: String? = "",
            @SerializedName("device_token")
            var deviceToken: String? = "",
            @SerializedName("device_type")
            var deviceType: String? = ""
        )
    }

    data class DropOff(
        @SerializedName("drop_off_address_id")
        var dropOffAddressId: String? = "",
        @SerializedName("latitude")
        var latitude: Double? = 0.0,
        @SerializedName("location")
        var location: String? = "",
        @SerializedName("longitude")
        var longitude: Double? = 0.0,
        @SerializedName("status")
        var status: String? = ""
    )

    data class ServiceInfo(
        @SerializedName("code")
        var code: String? = "",
        @SerializedName("icon")
        var icon: String? = "",
        @SerializedName("service_id")
        var serviceId: String? = "",
        @SerializedName("title")
        var title: String? = ""
    )

    data class VehicleInfo(
        @SerializedName("maker")
        var maker: Maker? = Maker(),
        @SerializedName("model")
        var model: Model? = Model(),
        @SerializedName("plate_no")
        var plateNo: String? = "",
        @SerializedName("vehicle_id")
        var vehicleId: String? = "",
        @SerializedName("year")
        var year: Year? = Year()
    ) {
        data class Maker(
            @SerializedName("icon")
            var icon: String? = "",
            @SerializedName("title")
            var title: String? = "",
            @SerializedName("vehicle_make_id")
            var vehicleMakeId: String? = ""
        )

        data class Model(
            @SerializedName("title")
            var title: String? = "",
            @SerializedName("vehicle_model_id")
            var vehicleModelId: String? = ""
        )

        data class Year(
            @SerializedName("title")
            var title: String? = "",
            @SerializedName("vehicle_year_id")
            var vehicleYearId: String? = ""
        )
    }

    data class VehicleRideOptionInfo(
        @SerializedName("caption")
        var caption: String? = "",
        @SerializedName("icon")
        var icon: String? = "",
        @SerializedName("max_seats")
        var maxSeats: Int? = 0,
        @SerializedName("min_seats")
        var minSeats: Int? = 0,
        @SerializedName("title")
        var title: String? = "",
        @SerializedName("vehicle_ride_option_id")
        var vehicleRideOptionId: String? = ""
    )

    data class FareInfo(
        @SerializedName("base_fare")
        var baseFare: Double? = 0.0,
        @SerializedName("cancellation_fee")
        var cancellationFee: Double? = 0.0,
        @SerializedName("cost_per_mile")
        var costPerMile: Double? = 0.0,
        @SerializedName("cost_per_min")
        var costPerMin: Double? = 0.0,
        @SerializedName("incentives_multiplier")
        var incentivesMultiplier: Double? = 0.0,
        @SerializedName("is_min_fare")
        var isMinFare: Boolean? = false,
        @SerializedName("min_base_fare")
        var minBaseFare: Double? = 0.0,
        @SerializedName("min_base_mile")
        var minBaseMile: Double? = 0.0,
        @SerializedName("subtotal")
        var subtotal: Double? = 0.0,
        @SerializedName("tax")
        var tax: Double? = 0.0,
        @SerializedName("technology_fee")
        var technologyFee: Double? = 0.0,
        @SerializedName("total")
        var total: Double? = 0.0,
        @SerializedName("total_mile_fare")
        var totalMileFare: Double? = 0.0,
        @SerializedName("total_min_fare")
        var totalMinFare: Double? = 0.0,
        @SerializedName("wait_fees")
        var waitFees: Double? = 0.0,
        @SerializedName("wait_fees_per_min")
        var waitFeesPerMin: Double? = 0.0
    )

    data class PassengerInfo(
        @SerializedName("average_ratings")
        var averageRatings: Float? = 0F,
        @SerializedName("email")
        var email: String? = "",
        @SerializedName("first_name")
        var firstName: String? = "",
        @SerializedName("last_name")
        var lastName: String? = "",
        @SerializedName("mobile")
        var mobile: String? = "",
        @SerializedName("photo")
        var photo: String? = "",
        @SerializedName("user_id")
        var userId: String? = ""
    )

    data class PickUp(
        @SerializedName("latitude")
        var latitude: Double? = 0.0,
        @SerializedName("location")
        var location: String? = "",
        @SerializedName("longitude")
        var longitude: Double? = 0.0
    )

    class Vehicle
}