package com.tourch.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.tourch.R
import com.tourch.application.Constants
import java.net.URLConnection
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {

    internal var dialog: Dialog? = null



    fun showToast(activity: Activity, msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    fun isPdfFile(file: String?): Boolean {
        if (file == null) return false
        val mimeType = URLConnection.guessContentTypeFromName(file)
        return mimeType != null && mimeType.startsWith("application/pdf")
    }

    fun isImageFile(path: String?): Boolean {
        if (path == null) return false
        val mimeType = URLConnection.guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("image")
    }


    fun getCurrentTime(): String {
        val localCalendar: Calendar = Calendar.getInstance()
        return localCalendar[Calendar.HOUR_OF_DAY]
            .toString() + ":" + localCalendar[Calendar.MINUTE]
    }

    fun getCurrentLocalDate(requiredDateFormat: String?): String? {
        //Get Current date in UTC FORMAT
        val currentTime = Date()
        val sdf = SimpleDateFormat(requiredDateFormat)

        return sdf.format(currentTime)
    }

    fun getChangeDateFormat(requiredDateFormat: String?,date: String?): String? {


        val dateFormatPrev = SimpleDateFormat("yyyy-MM-dd")
        val d = dateFormatPrev.parse(date)
        val dateFormat = SimpleDateFormat(requiredDateFormat)

        return dateFormat.format(d)
    }

    fun getCurrentDateDifference(toDate: String): Long {

        val current = getCurrentLocalDate("yyyy-MM-dd").toString()
        val formatted = current.format(current)

        val date1: Date
        val date2: Date
        val dates = SimpleDateFormat("yyyy-MM-dd")
        date1 = dates.parse(formatted)
        date2 = dates.parse(toDate)
        val difference: Long = date2.time - date1.time
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        return differenceDates
    }

    fun getFormattedDate(requiredDateFormat: String?, date: Date): String? {
        val sdf = SimpleDateFormat(requiredDateFormat)
        return sdf.format(date)
    }


    fun encryptString(str: String): String {
        val data = Security.encrypt(str)
        Log.e("params", "data==>$data")
        return data
    }

    fun encryptData(params: HashMap<String, Any>): HashMap<String, Any> {
        Log.e("params", "Gson().toJson(params)==>" + Gson().toJson(params))
        val data = Security.encrypt(Gson().toJson(params))
        Log.e("params", "data==>$data")

        val paramsEnc = HashMap<String, Any>()
        paramsEnc[Constants.Param.encrypt_data] = data

        return paramsEnc
    }

    fun decryptData(strEncryData: String): String? {
        val decryptString = Security.decrypt(strEncryData)
        Log.e("TAG", "decryptData==>$decryptString")
        printLog(decryptString)
        return decryptString
    }

    fun printLog(message: String) {
        val maxLogSize = 2000
        for (i in 0..message.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = if (end > message.length) message.length else end
            Log.d("finalresponse", "finalresponse==>" + message.substring(start, end))
        }
    }

    @Synchronized
    fun showLoading(activity: Activity) {
        if (dialog == null) {
            dialog = Dialog(activity)
        }
        dialog!!.setContentView(R.layout.progress_loader)
        // ((TextView) dialog.findViewById(R.id.tvMsg)).setText(getString(R.string.text_please_wait));
        dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun hideLoading(activity: Activity) {
        if ((dialog != null) and dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    fun isInternetAvailable(ctx: Context): Boolean {
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected && networkInfo.isConnectedOrConnecting

    }


}