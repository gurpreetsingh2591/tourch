package com.tourch.base

/*import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker*/
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.tourch.R
import com.tourch.application.Constants
import com.tourch.base.BaseActivity
import com.tourch.base.MyOnItemClickListener
import com.tourch.utils.GpsTracker

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.util.*
import kotlin.collections.HashMap

open class BaseFragment : Fragment() {

    var gpsTracker1: GpsTracker? = null
    public var baseActivity: BaseActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = activity as BaseActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
    }

    private fun initialization() {

        if (checkPermissions()) {
            gpsTracker1 = GpsTracker(baseActivity!!)
        }
    }

    public fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                baseActivity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                baseActivity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    fun encryptString(str : String) : String{
        return baseActivity!!.encryptString(str)
    }

    fun encryptData(params: HashMap<String, Any>): HashMap<String, Any> {
        return baseActivity!!.encryptData(params)
    }

    fun decryptData(strEncryData: String): String? {
        return baseActivity!!.decryptData(strEncryData)
    }

    open fun printLog(message: String) {
        val maxLogSize = 2000
        for (i in 0..message.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = if (end > message.length) message.length else end
            Log.d("finalresponse", "finalresponse==>"+message.substring(start, end))
        }
    }


    fun isLocationAllowed(): Boolean {
        return baseActivity!!.isLocationAllowed()
    }

    fun placeMarkerOnMap(mMap: GoogleMap?, location: LatLng, markerImg: Int, width :Int = resources.getDimension(
        com.intuit.sdp.R.dimen._22sdp).toInt(), height :Int = resources.getDimension(com.intuit.sdp.R.dimen._27sdp).toInt()): Marker {
        return baseActivity!!.placeMarkerOnMap(mMap, location, markerImg,width, height)
    }

    fun cityFromLatLong(latitude:Double,longitude:Double):String{
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        return addresses!![0].locality
    }

    fun addressFromLatLong(latitude:Double,longitude:Double):String{
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

        return addresses!![0].getAddressLine(0)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isPermissionNotGranted(permission: Array<String>): Boolean {
        return baseActivity!!.isPermissionNotGranted(permission)
    }

    fun requestPermission(
        permission: Array<String>,
        requestCode: Int,
        callbacks: MyOnItemClickListener,
    ) {
        baseActivity!!.requestPermission(permission, requestCode, callbacks)
    }

    fun createRequestBodyContent(data: String): RequestBody {
        return RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            "" + data
        )
    }

    fun msgDialog(msg: String, dialogTye: String? = Constants.Param.ERROR) {
        if (activity != null) {
            (activity as BaseActivity).msgDialog(msg, dialogTye)
        }
    }

    fun createRequestBodyContent(data: HashMap<String,Any>): RequestBody {
        return RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            "" + data
        )
    }

    fun checkGPSStatus(): Boolean {
        return baseActivity!!.checkGPSStatus()
    }

    fun validatEmail(email: String): Boolean {
        return baseActivity!!.validateEmail(email)
    }

    fun isInternetConnected(): Boolean {
        return baseActivity!!.isInternetConnected()
    }

    fun hideKeyboard(): Boolean {
        return baseActivity!!.hideKeyboard()
    }
    fun showKeyboard(): Boolean {
        return baseActivity!!.showKeyboard()
    }


    fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    fun changeTextColor(textView: AppCompatTextView, color: Int) {
        textView.setTextColor(ContextCompat.getColor(baseActivity!!, color))
    }

    fun showLoading() {
        baseActivity?.showLoading();
    }

    fun hideLoading() {
        baseActivity?.hideLoading();
    }

    fun clearAllFragment() {
        baseActivity?.clearAllFragment()
    }


    open fun loadImage(
        url: String?,
        imageView: ImageView,
        placeholder: Int? = R.drawable.placeholder,
        error: Int? = R.drawable.placeholder,
    ) {
        baseActivity!!.loadImage(
            url,
            imageView,
            placeholder,
            error
        )
    }

    fun replaceFragment(
        @NonNull fragment: Fragment,
        backStackName: Boolean = false,
        popStack: Boolean = false,
        animation: Boolean = false,
        @IdRes containerViewId: Int = R.id.main_content,
    ) {
        baseActivity!!.replaceFragment(
            fragment,
            backStackName,
            popStack,
            animation,
            containerViewId
        )
    }


    fun addFragment(
        @NonNull fragment: Fragment,
        backStackName: Boolean = false,
        aTAG: String = "",
        popBackstack: Boolean = false,
        isMainFrag: Boolean = false,
        @IdRes containerViewId: Int = R.id.main_content,

        ) {
        baseActivity!!.addFragment(
            fragment,
            backStackName,
            aTAG,
            popBackstack,
            isMainFrag,
            containerViewId
        )
    }

    open fun onTouchEvent(event: MotionEvent): Boolean {
        TODO("Not yet implemented")
    }


    inline fun <reified T : Any> Context.launchActivity(
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {},
    ) {

        val intent = newIntent<T>(this)
        intent.init()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, options)
        } else {
            startActivity(intent)
        }
    }

    inline fun <reified T : Any> newIntent(context: Context): Intent =
        Intent(context, T::class.java)

}