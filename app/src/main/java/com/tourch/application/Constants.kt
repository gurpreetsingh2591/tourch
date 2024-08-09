package com.tourch.application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object Constants {

    const val PAYPAL_CLIENT_ID: String =
        "AXkVVZQAO4eDnvfOJmNa4AB6ddB38Uz9hz8mRvLj6J3YFUG-UpvmPMW9RkcRlTymdY8LrxmQLVEZITXJ"
    const val PAYPAL_CLIENT_SECREAT: String =
        "EHeXcTntVOwLtOEEExHsEZNV5iaGIdvXnyG-pYnkBIKHO9rfYxDabAdVoY-CovqvVkTVVonDFlQT59tf"
    //var LOCATIONLIST = ArrayList<AddMultipleLocationModel>()

    /*var BASE_URL = "http://api.tourch.crypticpoint.com/api/"*/
    var BASE_URL = "https://api.tourchride.com/api/"  //Live Url//
    var BASE_PAYMENT_URL = "https://payzah.educator-app.com/"

    //request code
    val REQUEST_CODE_LOCATION: Int = 200
    const val cameraZoomLavel15_0_f = 15.0f
    const val CONTACTCODE = 40

    const val android = "android"
    const val PATTERN_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*)[a-zA-Z]{8,}$"

    const val prefName: String = "educator_pref"
    const val labelPrefName: String = "label_educator_pref"
    const val SUCCESS = "SUCCESS"
    const val ERROR = "ERROR"

    val RC_SIGN_IN: Int = 101
    const val LOCATION_INTERVAL = 5000
    const val FASTEST_LOCATION_INTERVAL = 3000

    const val CAMERA_REQUEST = 101
    const val LOCATION_REQUEST = 102
    const val STORAGE_REQUEST = 103
    //var viewHome by Delegates.notNull<View>()


    var SET_ADDRESS_MAP = 120
    var ADDRESS = ""
    var LATITUDE = 0.0
    var LONGITUDE = 0.0


    var SINGUP = "SINGUP"


    var _error: MutableLiveData<String> = MutableLiveData()
    var error: LiveData<String> = _error
    const val SHOW_LOADER = "showloader"


    //=================API Request prms====================


    object API_STATUS {
        const val SUCCESS = 200
    }

    object CmsCode {
        const val TERMS_AND_CONDITION = "TERMS_AND_CONDITION"
        const val PRIVACY_POLICY = "PRIVACY_POLICY"
        const val HOW_THE_LOYALTY_PROGRAM_WORKS = "HOW_THE_LOYALTY_PROGRAM_WORKS"
        const val HOW_INVITE_WORKS = "HOW_INVITE_WORKS"
        const val FEEL_GOOD = "FEEL_GOOD"
        const val ABOUT_US = "ABOUT_US"
    }

    object ServiceCaption {
        const val QUICK = "QUICK"
        const val CITY_TO_CITY = "CITY_TO_CITY"
        const val COMMUTE = "COMMUTE"
        const val LUXURY = "LUXURY"
        const val MY_DRIVER = "MY_DRIVER"
    }


    object Param {
        const val user_type = "user_type"
        const val code = "code"
        const val SUCCESS = "SUCCESS"
        const val ERROR = "ERROR"
        const val type = "type"
        const val mobile_country_code = "mobile_country_code"
        const val mobile = "mobile"
        const val country_code = "country_code"
        const val email = "email"
        const val password = "password"
        const val otp = "otp"
        const val social_id = "social_id"
        const val register_type = "register_type"
        const val last_name = "last_name"
        const val first_name = "first_name"
        const val name = "name"
        const val verify_mobile_id = "verify_mobile_id"
        const val cms_code = "cms_code"
        const val passenger_id = "passenger_id"
        const val driver_id = "driver_id"
        const val user_id = "user_id"
        const val new_password = "new_password"
        const val old_password = "old_password"
        const val profile_photo = "profile_photo"
        const val card_id = "card_id"
        const val card_name = "card_name"
        const val card_number = "card_number"
        const val exp_month = "exp_month"
        const val exp_year = "exp_year"
        const val cvc = "cvc"
        const val faq_category_id = "faq_category_id"
        const val service_id = "service_id"
        const val licence_number = "licence_number"
        const val licence_front = "licence_front"
        const val licence_back = "licence_back"
        const val commercial_insurance_number = "commercial_insurance_number"
        const val commercial_insurance_front = "commercial_insurance_front"
        const val commercial_insurance_back = "commercial_insurance_back"
        const val personal_insurance_number = "personal_insurance_number"
        const val personal_insurance_front = "personal_insurance_front"
        const val personal_insurance_back = "personal_insurance_back"
        const val notifications = "notifications"
        const val currency_id = "currency_id"
        const val vehicle_ride_option_id = "vehicle_ride_option_id"
        const val make = "make"
        const val model = "model"
        const val year = "year"
        const val capacity = "capacity"
        const val photo = "photo"
        const val title = "title"
        const val certificate_photo = "certificate_photo"
        const val certificate_id = "certificate_id"
        const val subject = "subject"
        const val message = "message"
        const val notification_log_id = "notification_log_id"
        const val availability_status = "availability_status"
        const val price = "price"
        const val subscription_code = "subscription_code"
        const val subscription_id = "subscription_id"
        const val payment_type = "payment_type"
        const val preference_id = "preference_id"
        const val preferences = "preferences"
        const val trip_id = "trip_id"
        const val status = "status"
        const val cancel_ride_reason_criteria_id = "cancel_ride_reason_criteria_id"
        const val reason = "reason"
        const val device_type = "device_type"
        const val device_name = "device_name"
        const val device_token = "device_token"
        const val trip_info_json = "trip_info_json"
        const val encrypt_data = "encrypt_data"
        const val from_user_id = "from_user_id"
        const val to_user_id = "to_user_id"
        const val from_user_type = "from_user_type"
        const val to_user_type = "to_user_type"
        const val rating = "rating"
        const val review = "review"
        const val vehicle_make_id = "vehicle_make_id"
        const val vehicle_model_id = "vehicle_model_id"
        const val vehicle_year_id = "vehicle_year_id"
        const val amount = "amount"
        const val comment = "comment"
        const val proposal_id = "proposal_id"
        const val app_version = "app_version"
        const val current_password = "current_password"
        const val service_ids = "service_ids"
        const val vehicle_plate_number = "vehicle_plate_number"
        const val driving_license_number = "driving_license_number"
        const val driving_license_front_picture = "driving_license_front_picture"
        const val driving_license_back_picture = "driving_license_back_picture"
        const val insurance_number = "insurance_number"
        const val insurance_front_picture = "insurance_front_picture"
        const val insurance_back_picture = "insurance_back_picture"
        const val social_security_number = "social_security_number"
        const val social_security_card_front_picture = "social_security_card_front_picture"
        const val social_security_card_back_picture = "social_security_card_back_picture"
        const val help_category_id = "help_category_id"
        const val pick_up_latitude = "pick_up_latitude"
        const val pick_up_longitude = "pick_up_longitude"
        const val drop_off = "drop_off"
        const val fare_info = "fare_info"
        const val pick_up_location = "pick_up_location"
        const val booking_date = "booking_date"
        const val number_of_travellers = "number_of_travellers"
        const val base_fare = "base_fare"
        const val technology_fee = "technology_fee"
        const val cost_per_mile = "cost_per_mile"
        const val cost_per_min = "cost_per_min"
        const val min_base_mile = "min_base_mile"
        const val min_base_fare = "min_base_fare"
        const val wait_fees = "wait_fees"
        const val wait_fees_per_min = "wait_fees_per_min"
        const val cancellation_fee = "cancellation_fee"
        const val incentives_multiplier = "incentives_multiplier"
        const val subtotal = "subtotal"
        const val total = "total"
        const val total_mile_fare = "total_mile_fare"
        const val total_min_fare = "total_min_fare"
        const val is_min_fare = "is_min_fare"
        const val tax = "tax"
        const val total_distance = "total_distance"
        const val formatted_total_distance = "formatted_total_distance"
        const val total_duration = "total_duration"
        const val formatted_total_duration = "formatted_total_duration"
        const val location = "location"
        const val latitude = "latitude"
        const val longitude = "longitude"
        const val book_for_someone_else_info = "book_for_someone_else_info"
        const val is_preferred_female_driver = "is_preferred_female_driver"
        const val experience = "experience"
        const val gender = "gender"
        const val address = "address"
        const val device_latitude = "device_latitude"
        const val device_longitude = "device_longitude"
        const val current_profile = "current_profile"
        const val page = "page"
        const val problem = "problem"
        const val file = "file"
        const val medias = "medias"
        const val unread_notifireceiverscations = "unread_notifications"
        const val canned_category_id = "canned_category_id"
        const val coupon_code = "coupon_code"
        const val fare = "fare"
        const val transaction_id = "transaction_id"
        const val receivers = "receivers"
        const val sender_user_id = "sender_user_id"
        const val receiver_mobile = "receiver_mobile"
        const val contacts = "contacts"
        const val pick_up_city = "pick_up_city"
        const val drop_off_location = "drop_off_location"
        const val drop_off_latitude = "drop_off_latitude"
        const val drop_off_longitude = "drop_off_longitude"
        const val drop_off_city = "drop_off_city"
        const val trip_at = "trip_at"
        const val number_of_passengers = "number_of_passengers"
        const val description = "description"
        const val city = "city"
        const val fixed_ride_id = "fixed_ride_id"
        const val about_me = "about_me"
        const val from_city = "from_city"
        const val to_city = "to_city"
        const val pick_up = "pick_up"
    }


    object Preference {
        const val LANGUAGE_ID = "LANGUAGE_ID"
        const val LANGUAGE_TITLE = "LANGUAGE_TITLE"
        const val COUNTRY_ID = "COUNTRY_ID"
        const val CURRENCY_ID = "CURRENCY_ID"
        const val LANGUAGE_CODE = "LANGUAGE_CODE"
        const val FILEPATH = "FILEPATH"
    }

    object Usertype {
        const val PASSENGER = "passenger"
        const val DRIVER = "driver"
        const val BUSINESS = "business"
    }


}