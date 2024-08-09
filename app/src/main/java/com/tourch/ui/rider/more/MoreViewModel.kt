package com.tourch.ui.rider.more

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

class MoreViewModel : ViewModel() {
    var responseSendOtpData: MutableLiveData<WebResponse<EncryptedDataResponse>> = MutableLiveData()

    var isLoading = MutableLiveData<Boolean>()
    var isViewEnable = MutableLiveData<Boolean>()
    var errorHandler: ErrorHandler? = ErrorHandler()

    /*
     * method to call Send Otp api
     * */
    fun callSendOtpAPI(encryptData: Map<String, Any?>) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

            ApiClient().getClient().callSendOtpApi(encryptData).enqueue(object : Callback<EncryptedDataResponse?> {
                override fun onResponse(call: Call<EncryptedDataResponse?>, response: Response<EncryptedDataResponse?>) {
                    isLoading.postValue(false)
                    isViewEnable.postValue(false)
                    if (response.isSuccessful && response.body() != null) {
                        val result: EncryptedDataResponse? = response.body()
                        if (result?.status ==200)
                            responseSendOtpData.setValue(WebResponse(Status.SUCCESS, result, null)
                        ) else {
                            responseSendOtpData.setValue(WebResponse(Status.FAILURE, null, response.body()!!.payload.iv))
                        }
                    } else {
                        responseSendOtpData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                    }
                }

                override fun onFailure(call: Call<EncryptedDataResponse?>, t: Throwable) {
                    isLoading.postValue(false)
                    isViewEnable.postValue(false)
                     val errorMsg = errorHandler!!.reportError(t)
                    responseSendOtpData.value = WebResponse(Status.FAILURE, null, errorMsg)
                }
            })
    }


}