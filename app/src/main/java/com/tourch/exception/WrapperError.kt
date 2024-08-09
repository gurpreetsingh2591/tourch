package com.tourch.exception

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tourch.annotation.Constants

class WrapperError : RuntimeException {

    @Expose
    @SerializedName(Constants.HTTP_CODE)
    private var statusCode: Long? = null

    @Expose
    @SerializedName(Constants.MESSAGE)
    private var messages: String? = null

    constructor(statusCode: Long, message: String?) {
        this.statusCode = statusCode
        this.messages = message
    }

    constructor(statusCode: Long) {
        this.statusCode = statusCode
    }

    fun getStatusCode(): Long? {
        return statusCode
    }

    fun setStatusCode(statusCode: Long?) {
        this.statusCode = statusCode
    }

    fun getMessages(): String? {
        return messages
    }

    fun setMessage(message: String?) {
        this.messages = message
    }
}