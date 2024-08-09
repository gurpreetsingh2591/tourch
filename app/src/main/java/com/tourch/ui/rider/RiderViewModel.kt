package com.tourch.ui.rider

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tourch.annotation.Status
import com.tourch.exception.ErrorHandler
import com.tourch.retrofit.ApiClient
import com.tourch.retrofit.EncryptedDataResponse
import com.tourch.retrofit.WebResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiderViewModel : ViewModel() {
    var responseEstimateFareData: MutableLiveData<WebResponse<EncryptedDataResponse>> = MutableLiveData()
    var responseCityToCityData: MutableLiveData<WebResponse<EncryptedDataResponse>> = MutableLiveData()
    var responseServiceData: MutableLiveData<WebResponse<EncryptedDataResponse>> = MutableLiveData()
    var responseBookingData: MutableLiveData<WebResponse<EncryptedDataResponse>> = MutableLiveData()
    var responseOngoingData: MutableLiveData<WebResponse<EncryptedDataResponse>> = MutableLiveData()
    var responseCancelReasonData: MutableLiveData<WebResponse<EncryptedDataResponse>> = MutableLiveData()
    var responseCancelData: MutableLiveData<WebResponse<EncryptedDataResponse>> = MutableLiveData()

    var isLoading = MutableLiveData<Boolean>()
    var isViewEnable = MutableLiveData<Boolean>()
    var errorHandler: ErrorHandler? = ErrorHandler()

    fun callEstimateFareAPI(encryptData: Map<String, Any?>) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

            ApiClient().getClient().callGetEstimateFareApi(encryptData).enqueue(object : Callback<EncryptedDataResponse?> {
                override fun onResponse(call: Call<EncryptedDataResponse?>, response: Response<EncryptedDataResponse?>) {
                    isLoading.postValue(false)
                    isViewEnable.postValue(false)
                    if (response.isSuccessful && response.body() != null) {
                        val result: EncryptedDataResponse? = response.body()
                        if (result?.status ==200)
                            responseEstimateFareData.setValue(WebResponse(Status.SUCCESS, result, null)
                        ) else {
                            responseEstimateFareData.setValue(WebResponse(Status.FAILURE, null, response.body()!!.payload.iv))
                        }
                    } else {
                        responseEstimateFareData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                    }
                }

                override fun onFailure(call: Call<EncryptedDataResponse?>, t: Throwable) {
                    isLoading.postValue(false)
                    isViewEnable.postValue(false)
                     val errorMsg = errorHandler!!.reportError(t)
                    responseEstimateFareData.value = WebResponse(Status.FAILURE, null, errorMsg)
                }
            })
    }
    fun callCityToCityAPI(encryptData: Map<String, Any?>) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        ApiClient().getClient().callRoutesCityToCityTripListApi(encryptData).enqueue(object : Callback<EncryptedDataResponse?> {
            override fun onResponse(call: Call<EncryptedDataResponse?>, response: Response<EncryptedDataResponse?>) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: EncryptedDataResponse? = response.body()
                    if (result?.status ==200)
                        responseCityToCityData.setValue(WebResponse(Status.SUCCESS, result, null)
                        ) else {
                        responseCityToCityData.setValue(WebResponse(Status.FAILURE, null, response.body()!!.payload.iv))
                    }
                } else {
                    responseCityToCityData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                }
            }

            override fun onFailure(call: Call<EncryptedDataResponse?>, t: Throwable) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseCityToCityData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }
    fun callGetServiceListAPI() {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        ApiClient().getClient().callGetServiceListApi().enqueue(object : Callback<EncryptedDataResponse?> {
            override fun onResponse(call: Call<EncryptedDataResponse?>, response: Response<EncryptedDataResponse?>) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: EncryptedDataResponse? = response.body()
                    if (result?.status ==200)
                        responseServiceData.setValue(WebResponse(Status.SUCCESS, result, null)
                        ) else {
                        responseServiceData.setValue(WebResponse(Status.FAILURE, null, response.body()!!.payload.iv))
                    }
                } else {
                    responseServiceData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                }
            }

            override fun onFailure(call: Call<EncryptedDataResponse?>, t: Throwable) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseServiceData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }
    fun callBookNowTripAPI(encryptData: Map<String, Any?>) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        ApiClient().getClient().callBookTripApi(encryptData).enqueue(object : Callback<EncryptedDataResponse?> {
            override fun onResponse(call: Call<EncryptedDataResponse?>, response: Response<EncryptedDataResponse?>) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: EncryptedDataResponse? = response.body()
                    if (result?.status ==200)
                        responseBookingData.setValue(WebResponse(Status.SUCCESS, result, null)
                        ) else {
                        responseBookingData.setValue(WebResponse(Status.FAILURE, null, response.body()!!.payload.iv))
                    }
                } else {
                    responseBookingData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                }
            }

            override fun onFailure(call: Call<EncryptedDataResponse?>, t: Throwable) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseBookingData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }
    fun callOngoingTripAPI(encryptData: Map<String, Any?>) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        ApiClient().getClient().callOnGoingTripApi(encryptData).enqueue(object : Callback<EncryptedDataResponse?> {
            override fun onResponse(call: Call<EncryptedDataResponse?>, response: Response<EncryptedDataResponse?>) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: EncryptedDataResponse? = response.body()
                    if (result?.status ==200)
                        responseOngoingData.setValue(WebResponse(Status.SUCCESS, result, null)
                        ) else {
                        responseOngoingData.setValue(WebResponse(Status.FAILURE, null, response.body()!!.payload.iv))
                    }
                } else {
                    responseOngoingData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                }
            }

            override fun onFailure(call: Call<EncryptedDataResponse?>, t: Throwable) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseOngoingData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }
    fun callCancelReasonTripAPI(encryptData: Map<String, Any?>) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        ApiClient().getClient().callGetCancelReasonListApi(encryptData).enqueue(object : Callback<EncryptedDataResponse?> {
            override fun onResponse(call: Call<EncryptedDataResponse?>, response: Response<EncryptedDataResponse?>) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: EncryptedDataResponse? = response.body()
                    if (result?.status ==200)
                        responseCancelReasonData.setValue(WebResponse(Status.SUCCESS, result, null)
                        ) else {
                        responseCancelReasonData.setValue(WebResponse(Status.FAILURE, null, response.body()!!.payload.iv))
                    }
                } else {
                    responseCancelReasonData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                }
            }

            override fun onFailure(call: Call<EncryptedDataResponse?>, t: Throwable) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseCancelReasonData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }
    fun callCancelTripAPI(encryptData: Map<String, Any?>) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        ApiClient().getClient().callTripCancelApi(encryptData).enqueue(object : Callback<EncryptedDataResponse?> {
            override fun onResponse(call: Call<EncryptedDataResponse?>, response: Response<EncryptedDataResponse?>) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result: EncryptedDataResponse? = response.body()
                    if (result?.status ==200)
                        responseCancelData.setValue(WebResponse(Status.SUCCESS, result, null)
                        ) else {
                        responseCancelData.setValue(WebResponse(Status.FAILURE, null, response.body()!!.payload.iv))
                    }
                } else {
                    responseCancelData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                }
            }

            override fun onFailure(call: Call<EncryptedDataResponse?>, t: Throwable) {
                isLoading.postValue(false)
                isViewEnable.postValue(false)
                val errorMsg = errorHandler!!.reportError(t)
                responseCancelData.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }

}