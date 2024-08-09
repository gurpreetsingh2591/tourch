package com.tourch.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtils {

    private const val SHARED_PREFERENCES = "Tourch"
    private var sPreferences: SharedPreferences? = null
    private const val IS_USER_LOGIN = "login"
    private const val USER_ID = "user_id"
    private const val USER_TYPE = "user_type"
    private const val USER_DATA = "user_data"

    fun init(context: Context?) {
        sPreferences = context?.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun isUserLogin(): Boolean? {
        return sPreferences?.getBoolean(IS_USER_LOGIN, false)
    }

    fun getUserId(): String? {
        return sPreferences?.getString(USER_ID, "")
    }

    fun getUserType(): String? {
        return sPreferences?.getString(USER_TYPE, "")
    }

    fun setIsUserLogin(status: Boolean) {
        sPreferences?.edit()
            ?.putBoolean(IS_USER_LOGIN, status)
            ?.apply()
    }

    fun setUserData(){

    }

    fun clearUser() {
        sPreferences?.edit()
            ?.clear()
            ?.apply()
    }

}