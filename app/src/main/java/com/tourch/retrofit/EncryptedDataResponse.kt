package com.tourch.retrofit


import com.google.gson.annotations.SerializedName

data class EncryptedDataResponse(
    @SerializedName("error")
    val error: Any,
    @SerializedName("payload")
    val payload: Payload,
    @SerializedName("status")
    val status: Int
) {
    data class Payload(
        @SerializedName("data")
        val `data`: String,
        @SerializedName("iv")
        val iv: String
    )
}