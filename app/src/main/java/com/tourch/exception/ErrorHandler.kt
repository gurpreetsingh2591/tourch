package com.tourch.exception

import com.google.gson.JsonSyntaxException
import com.tourch.annotation.Constants
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection

class  ErrorHandler {
    fun reportError(error: Throwable): String? {
        return if (error is HttpException) {
            when (error.code()) {
                HttpsURLConnection.HTTP_UNAUTHORIZED -> Constants.UN_AUTH_USER
                HttpsURLConnection.HTTP_FORBIDDEN -> Constants.FORBIDDEN
                HttpsURLConnection.HTTP_INTERNAL_ERROR -> Constants.INTERNAL_ERROR
                HttpsURLConnection.HTTP_BAD_REQUEST ->Constants.BAD_REQUEST
                else -> error.getLocalizedMessage()
            }
        } else if (error is WrapperError) {
            error.message
        } else if (error is JsonSyntaxException) {
            Constants.API_NOT_RESPONDING
        } else if (error is SocketTimeoutException) {
            Constants.CANNOT_CONNECT_TO_SERVER
        }  else if (error is UnknownHostException) {
            Constants.CANNOT_CONNECT_TO_SERVER
        } else {
            error.message
        }
    }

    fun reportError(errorCode: Int): String {
        var errorMessage = ""
        when (errorCode) {
            HttpsURLConnection.HTTP_UNAUTHORIZED -> errorMessage = Constants.UN_AUTH_USER
            HttpsURLConnection.HTTP_FORBIDDEN -> errorMessage = Constants.FORBIDDEN
            HttpsURLConnection.HTTP_INTERNAL_ERROR -> errorMessage = Constants.INTERNAL_ERROR
            HttpsURLConnection.HTTP_BAD_REQUEST -> errorMessage = Constants.BAD_REQUEST
        }
        return errorMessage
    }
}