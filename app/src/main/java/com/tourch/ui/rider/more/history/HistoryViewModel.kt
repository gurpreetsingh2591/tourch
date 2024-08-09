package com.tourch.ui.rider.more.history

import android.util.Log
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

class HistoryViewModel : ViewModel() {
    var responseTripHistoryData: MutableLiveData<WebResponse<EncryptedDataResponse>> = MutableLiveData()

    var isLoading = MutableLiveData<Boolean>()
    var isViewEnable = MutableLiveData<Boolean>()
    var errorHandler: ErrorHandler? = ErrorHandler()

    /*
     * method to call History api
     * */
    fun callTripHistoryAPI(encryptData: Map<String, Any?>) {
        isLoading.postValue(true)
        isViewEnable.postValue(true)

        Log.e("testing","hngf1321313")


        ApiClient().getClient().callTripHistoryApi(encryptData).enqueue(object : Callback<EncryptedDataResponse?> {
                override fun onResponse(call: Call<EncryptedDataResponse?>, response: Response<EncryptedDataResponse?>) {
                    isLoading.postValue(false)
                    isViewEnable.postValue(false)
                    if (response.isSuccessful && response.body() != null) {
                        val result: EncryptedDataResponse? = response.body()
                        if (result?.status ==200){
                            Log.e("testing","1321313")
                            responseTripHistoryData.setValue(WebResponse(Status.SUCCESS, result, null)
                        ) }else {
                            Log.e("testing","56756756756")

                            responseTripHistoryData.setValue(WebResponse(Status.FAILURE, null, response.body()!!.payload.iv))
                        }
                    } else {
                        Log.e("testing","89089087078")

                        responseTripHistoryData.setValue(WebResponse(Status.FAILURE, null, response.message()))
                    }
                }

                override fun onFailure(call: Call<EncryptedDataResponse?>, t: Throwable) {
                    isLoading.postValue(false)
                    isViewEnable.postValue(false)
                     val errorMsg = errorHandler!!.reportError(t)
                    responseTripHistoryData.value = WebResponse(Status.FAILURE, null, errorMsg)

                    Log.e("testing","132187878078087313")

                }
            })
    }
}