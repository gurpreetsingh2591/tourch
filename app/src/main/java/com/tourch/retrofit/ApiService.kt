package com.tourch.retrofit

import com.tourch.application.Constants
import com.tourch.ui.mapCustomView.map_poliline.Result
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * All the API services are here.
 */
interface ApiService {

    @JvmSuppressWildcards
    @GET("maps/api/directions/json")
    fun getDirectionsSingle(
        @Query("mode") mmode: String,
        @Query("transit_routing_preference") routingPreference: String,
        @Query("origin") morigin: String,
        @Query("destination") mdestination: String,
        @Query("waypoints") mwaypoints: String?,
        @Query("key") apiKey: String,
    ): Single<Result>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("otp/send")
    fun callSendOtpApi(
        @FieldMap params: Map<String, Any?>
    ): Call<EncryptedDataResponse>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("otp/verify")
    fun callVerifyOtpApi(
        @FieldMap params: Map<String, Any?>
    ): Call<EncryptedDataResponse>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("user/signup")
    fun callSignUpApi(
        @FieldMap params: Map<String, Any?>
    ): Call<EncryptedDataResponse>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("user/signin")
    fun callSignInApi(
        @FieldMap params: Map<String, Any?>
    ): Call<EncryptedDataResponse>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("user/log-out")
    fun callLogOutApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("user/profile")
    fun callUserProfileApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @PUT("user/update-profile/{id}")
    fun callUpdateProfileApi(
        @Path("id") mid: String,
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @Multipart
    @POST("url/from-file")
    fun callGetUrlFromFileApi(
        @Part(Constants.Param.type) mType: RequestBody,
        @Part key: MultipartBody.Part?
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("user/change-password")
    fun callChangePasswordApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("service/list")
    fun callGetServiceListApi(
    ): Call<EncryptedDataResponse>

    @JvmSuppressWildcards
    @GET("estimated/fare-and-services")
    fun callGetEstimateFareApi(
        @QueryMap params: Map<String, Any?>
    ): Call<EncryptedDataResponse>

    @JvmSuppressWildcards
    @GET("city-to-city/routes/list-city-trips")
    fun callRoutesCityToCityTripListApi(
        @QueryMap params: Map<String, Any?>
    ): Call<EncryptedDataResponse>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("trip/book")
    fun callBookTripApi(
        @FieldMap params: Map<String, Any?>
    ): Call<EncryptedDataResponse>

    @JvmSuppressWildcards
    @GET("trip/ongoing")
    fun callOnGoingTripApi(
        @QueryMap params: Map<String, Any?>
    ): Call<EncryptedDataResponse>
    ///after that keep data and use firebase

    //Cancelation
    @JvmSuppressWildcards
    @GET("cancellation-reason/list")
    fun callGetCancelReasonListApi(
        @QueryMap params: Map<String, Any?>
    ): Call<EncryptedDataResponse>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("trip/cancel")
    fun callTripCancelApi(
        @FieldMap params: Map<String, Any?>
    ): Call<EncryptedDataResponse>

    @JvmSuppressWildcards
    @GET("trip/history")
    fun callTripHistoryApi(
        @QueryMap params: Map<String, Any?>
    ): Call<EncryptedDataResponse>




























    @JvmSuppressWildcards
    @GET("vehicle-type/list")
    fun callGetVehicleTypeApi(
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("maker/list")
    fun callGetVehicleMakeListApi(
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("model/list")
    fun callGetVehicleModelListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("year/list")
    fun callGetVehicleYearListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("vehicle/add")
    fun callAddVehicleApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("certificate/list")
    fun callCertificateListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("certificate/add")
    fun callAddCertificateApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "certificate/remove/{id}", hasBody = true)
    fun callRemoveCertificateApi(
        @Path("id") mId: String,
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @PUT("certificate/edit/{id}")
    fun callEditCertificateApi(
        @Path("id") mid: String,
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("notification/list")
    fun callGetNotificationListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "notification/remove/{id}", hasBody = true)
    fun callRemoveNotificationApi(
        @Path("id") mId: String,
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("help-category/list")
    fun callFaqCategoryApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("help/list")
    fun callFaqQuestionsApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("setting/list")
    fun callSettingListApi(
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("contact-us/submit")
    fun callSupportContactUsApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("preference/list")
    fun callGetPreferencesListApi(
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("trip/details")
    fun callGetTripDetailsApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("vehicle/list")
    fun callGetVehicleListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>


    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("trip/action")
    fun callTripActionApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("trip/rating")
    fun callTripRatingApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("card/list")
    fun callGetCardListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("card/add")
    fun callAddCardApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("address/add")
    fun callAddAddressApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("address/list")
    fun callGetAddressListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("address/for-trip")
    fun callGetAddressForTripApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "address/remove/{id}", hasBody = true)
    fun callRemoveAddressApi(
        @Path("id") mId: String,
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "card/remove/{id}", hasBody = true)
    fun callRemoveCardApi(
        @Path("id") mId: String,
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("cms/list")
    fun callCmsApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("my/rides")
    fun callGetMyRidesApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "emergency-contact/remove/{contactid}", hasBody = true)
    fun callRemoveEmergencyContactApi(
        @Path("contactid") contactids: String,
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("emergency-contact/list")
    fun callEmergencyContactListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @Multipart
    @POST("emergency-contact/add")
    fun callAddEmergencyContactApi(
        @Part(Constants.Param.user_id) muser_id: RequestBody,
        @Part(Constants.Param.name) mname: RequestBody,
        @Part(Constants.Param.mobile) mmobile: RequestBody,
        @Part photo: MultipartBody.Part?
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("report-problem/registration")
    fun callReportProblemApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("favourite-driver/add")
    fun callAddFavouriteDriverApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "favourite-driver/remove/{id}", hasBody = true)
    fun callRemoveFavouriteDriverApi(
        @Path("id") mId: String,
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("favourite-driver/list")
    fun callFavouriteDriverListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("subscription/list")
    fun callGetSubscriptionListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("subscription/purchase")
    fun callPurchaseSubscriptionApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @DELETE("account/remove/{id}")
    fun callRemoveAccountApi(
        @Path("id") id: String
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("canned-category/list")
    fun callGetCannedCategoryListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("canned-question/list")
    fun callGetCannedListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("coupon/verify")
    fun callApplyPromoCodeApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("split-fare/invitation")
    fun callSplitFareApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("split-fare/accepted")
    fun callAcceptSplitFareApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("split-fare/rejected")
    fun callRejectSplitFareApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("split-fare/recently-used")
    fun callGetRecentContactsApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("split-fare/filter-contacts")
    fun callGetFilterContactsApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("fixed-ride/add")
    fun callCreateCityToCityTripDriverApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("fixed-ride/list")
    fun callCityToCityTripListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("fixed-ride-passenger/list")
    fun callPassengerCityToCityTripListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("fixed-ride-passenger/details")
    fun callPassengerCityToCityTripDetailsApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("fixed-ride/display")
    fun callDriverCityToCityTripDetailsApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @PUT("fixed-ride/edit/{id}")
    fun callEditCityToCityTripDriverApi(
        @Path("id") mid: String,
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "fixed-ride/remove/{id}", hasBody = true)
    fun callRemoveCityToCityTripDriverApi(
        @Path("id") mId: String,
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("city-to-city/trip/book")
    fun callBookCityToCityTripPassengerApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("trip/chat-push")
    fun callChatPushApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("city-to-city/trip/action")
    fun callCityToCityTripActionApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("city-to-city/trip/ongoing")
    fun callCityToCityOnGoingTripApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("city-to-city/my/rides")
    fun callScheduledCityToCityTripListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("city-to-city/trip/cancel")
    fun callCityToCityTripCancelApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("city-to-city/routes/cities")
    fun callRoutesCitiesListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>



    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("city-to-city/trip/request-proposal")
    fun callRequestProposalPassengerCityToCityApi(
        @FieldMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>

    @JvmSuppressWildcards
    @GET("city-to-city/proposal/list")
    fun callDriverProposalCityToCityTripListApi(
        @QueryMap params: Map<String, Any?>
    ): Observable<Response<EncryptedDataResponse>>


}

