package com.tourch.base

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.tourch.BuildConfig
import com.tourch.R
import com.tourch.application.Constants
import com.tourch.ui.rider.contacts.ContactModel
import com.tourch.utils.MessageDialog
import com.tourch.utils.Security
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.roundToInt


open class BaseActivity : AppCompatActivity() {


    val msgDialog = MessageDialog.getInstance()!!
    val internetMsgDialog = MessageDialog.getInstance()
    internal var dialog: Dialog? = null
    private val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    var callback: MyOnItemClickListener? = null
    private val CHECK_CAMERA = 300
    private val CHECK_STORAGE = 400
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private val RESULT_LOAD_IMAGE = 2
    var imageUri: Uri? = null
    var imgPath: String? = null
    private val EOF = -1
    var imgView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isPermissionNotGranted(permission: Array<String>): Boolean {
        var flag = false
        for (i in permission.indices) {
            if (checkSelfPermission(permission.get(i)) != PackageManager.PERMISSION_GRANTED) {
                flag = true
                break
            }
        }
        return flag
    }

    fun requestPermission(
        permission: Array<String>,
        requestCode: Int,
        callbacks: MyOnItemClickListener,
    ) {
        callback = callbacks
        ActivityCompat.requestPermissions(this, permission, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == CHECK_CAMERA || requestCode == CHECK_STORAGE || requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
                callback!!.onItemClicked(requestCode)
            }
        }
    }

    fun checkGPSStatus(): Boolean {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    open fun validateEmail(email: String?): Boolean {
        val pattern =
            Pattern.compile(EMAIL_PATTERN)
        val matcher =
            pattern.matcher(email)
        return matcher.matches()
    }

    fun isLocationAllowed(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun hideKeyboard(): Boolean {
        try {
            val view = this.currentFocus
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view!!.windowToken, 0)
        } catch (ignored: RuntimeException) {
        }
        return false
    }

    fun showKeyboard(): Boolean {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
        return false
    }

    fun showMessageDialog(
        msg: String,
        msgOk: String? = null,
        msgCancel: String? = null,
        isConfirm: Boolean = false,
    ) {
        val bundle = Bundle()
        bundle.putString("tvMsgText", msg)
        if (isConfirm) {
            bundle.putString("okTxt", msgOk)
            bundle.putString("cancelTxt", msgCancel)
        } else {
            bundle.putString("okTxt", msgOk)
        }

        msgDialog.arguments = bundle
        if (msgDialog != null && msgDialog.isAdded)
            msgDialog.dismiss()

        if (!this.isFinishing) {
            msgDialog.show(this.supportFragmentManager, "")
        }
    }

    fun shareKit(shareBodyText: String, subject: String, SharingOption: String) {
        val sharingIntent = Intent(
            Intent.ACTION_SEND
        )
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText)
        startActivity(Intent.createChooser(sharingIntent, SharingOption))
    }

    var timer: CountDownTimer? = null

    var isConnected: Boolean = false

    fun msgDialog(msg: String, dialogType: String? = Constants.Param.ERROR) {
        try {
            var dialogMsg = MessageDialog.getInstance()
            val bundle = Bundle()
            bundle.putString("tvMsgText", msg)
            bundle.putString("okTxt", "OK")
            bundle.putString("msgType", "" + dialogType)
            dialogMsg.arguments = bundle
//        if (dialogType.equals(Constants.Param.ERROR)) {
//            setStatusBar(ContextCompat.getColor(baseContext!!, R.color.colorPrimary))
//        } else {
//            setStatusBar(ContextCompat.getColor(baseContext!!, R.color.colorPrimary))
//        }
            if (dialogMsg.isAdded) {
                return
            }
            Log.e("TAG", "msgDialog-> show()")
            dialogMsg.show(supportFragmentManager, "")
            timer = object : CountDownTimer(3500, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
//                setStatusBar(ContextCompat.getColor(baseContext!!, R.color.transparent))
                    if (timer != null) {
                        timer!!.cancel()
                    }
                    try {
                        if (dialogMsg.isVisible)
                            dialogMsg!!.dismiss()
                    } catch (e: java.lang.Exception) {
                    }

                }
            }.start()
            dialogMsg.setListener {
                dialogMsg.dismiss()
                if (dialogMsg.isVisible)
                    if (timer != null) {
                        timer!!.cancel()
                    }
            }
        } catch (e: Exception) {
        }
    }

    fun isInternetConnected(): Boolean {
        isConnected = GlobalMethods.isInternetAvailable(baseContext)
        if (!isConnected) {
            showInternetDialog()
        }
        return isConnected

    }

    private fun showInternetDialog() {
        val bundle = Bundle()
        bundle.putString(
            "tvMsgText",
            "Seems like you don't have an active internet connection. Please check your connection and try again."
        )
        bundle.putString("okTxt", "OK")
        bundle.putString("msgType", "" + Constants.Param.ERROR)
        internetMsgDialog.arguments = bundle
//        setStatusBar(ContextCompat.getColor(baseContext!!, R.color.colorPrimary))
        if (internetMsgDialog.isAdded) {
            return
        }

        try {
            Log.e("TAG", "internetMsgDialog-> 1")
            if (!internetMsgDialog.isVisible && !internetMsgDialog.isAdded) {
                Log.e("TAG", "internetMsgDialog-> show()")
                internetMsgDialog.show(supportFragmentManager, "")
            }

            Log.e("TAG", "internetMsgDialog-> 2")
        } catch (e: Exception) {
        }
        timer = object : CountDownTimer(3500, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                if (timer != null) {
                    timer!!.cancel()
                }
                try {
                    if (internetMsgDialog.isVisible)
                        internetMsgDialog!!.dismiss()
                } catch (e: java.lang.Exception) {

                }

            }
        }.start()
        internetMsgDialog.setListener {
            if (timer != null) {
                timer!!.cancel()
            }
            try {
                if (internetMsgDialog.isVisible)
                    internetMsgDialog!!.dismiss()
            } catch (e: java.lang.Exception) {

            }
        }
    }

    public fun setStatusBar(color: Int, isTextColorWhite: Boolean? = false) {
        if (isTextColorWhite!!) {
            window.decorView.systemUiVisibility = window.decorView
                .systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() //set status text  light
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
        }
    }

    @Synchronized
    fun showLoading() {
        if (dialog == null) {
            dialog = Dialog(this)
        }
        dialog!!.setContentView(R.layout.progress_loader)
        // ((TextView) dialog.findViewById(R.id.tvMsg)).setText(getString(R.string.text_please_wait));
        dialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun hideLoading() {
        if ((dialog != null) and dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    fun clearAllFragment() {
        val fm = this.supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }

    public open fun loadImage(
        url: String?,
        imageView: ImageView,
        placeholder: Int? = R.drawable.placeholder,
        error: Int? = R.drawable.placeholder, width: Int? = null, height: Int? = null
    ) {
        if (width == null) {
            Glide.with(applicationContext!!)
                .load(url)
                .apply(RequestOptions().placeholder(placeholder!!))
                .error(error!!)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("ErrrorGlide", "ErrrorGlide==" + e!!.causes.toString())
                        return false // important to return false so the error placeholder can be placed
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(imageView)
        } else {
            Glide.with(applicationContext!!)
                .load(url)
                .apply(RequestOptions().placeholder(placeholder!!))
                .error(error!!)
                .override(width!!, height!!)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("ErrrorGlide", "ErrrorGlide==" + e!!.causes.toString())
                        return false // important to return false so the error placeholder can be placed
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(imageView)
        }
    }

    fun placeMarkerOnMap(
        mMap: GoogleMap?,
        location: LatLng,
        markerImg: Int,
        width: Int = baseContext!!.resources.getDimension(com.intuit.sdp.R.dimen._22sdp).toInt(),
        height: Int = baseContext!!.resources.getDimension(com.intuit.sdp.R.dimen._27sdp).toInt(),
    ): Marker {

        val bitmapdraw = baseContext!!.resources.getDrawable(markerImg) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(
            b,
            width,
            height,
            false
        )

        Log.d("TAG", "placeMarkerOnMap: $location")

        val markerOptions =
            MarkerOptions().position(location).title("")
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        val marker = mMap!!.addMarker(markerOptions)
        //val titleStr = getAddress(location)  // add these two lines

        return marker!!
    }

    fun placeMarkerOnMapWithSize(
        mMap: GoogleMap,
        mMarker: Marker?,
        latLng: LatLng,
        markerImg: Int,
        markerHeight: Int,
        markerWeight: Int,
        isUpdateCamera: Boolean,
        cameraZoomLavel: Float = 15.0F, isOffset: Boolean = false,
        title: String? = null,
        snippet: String? = null
    ): Marker? {

        val bitmapdraw = ContextCompat.getDrawable(this, markerImg) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, markerWeight, markerHeight, false)

        val markerOptions =
            MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .title(if (title != null) title else null)
                .snippet(if (snippet != null) snippet else null)

        var marker = mMarker

        try {
            marker.let {
                marker!!.position = latLng
            }
        } catch (e: Exception) {
            marker = mMap.addMarker(markerOptions)
        }


        if (isUpdateCamera) {
            val cameraPosition = CameraPosition.fromLatLngZoom(latLng, cameraZoomLavel)
            val cu = CameraUpdateFactory.newCameraPosition(cameraPosition)
            mMap.animateCamera(cu)
        }
        return marker
    }

    @SuppressLint("Range")
    open fun getContacts(): ArrayList<ContactModel>? {
        val list: ArrayList<ContactModel> = ArrayList()
        val contentResolver: ContentResolver = baseContext.contentResolver
        val cursor: Cursor =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)!!
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                val id: String =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val cursorInfo: Cursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID.toString() + " = ?",
                        arrayOf(id),
                        null
                    )!!
                    while (cursorInfo.moveToNext()) {
                        val info = ContactModel()
                        info.id = id
                        info.name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        info.mobileNumber =
                            cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        if (!checkPhoneNumber(info.mobileNumber, list))
                            list.add(info)
                    }
                    cursorInfo.close()
                }
            }
            cursor.close()
        }
        return list
    }

    private fun checkPhoneNumber(mobileNumber: String?, list: ArrayList<ContactModel>): Boolean {
        if (list.size > 0) {
            for (obj in list) {
                if (obj.mobileNumber == mobileNumber)
                    return true
            }
        }
        return false
    }


    open fun getCityFromLatlong(LATITUDE: Double, LONGITUDE: Double): String? {
        var strAdd = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)!!
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                strAdd = returnedAddress.locality
            } else {
                Log.w("loction address", "No Address returned!")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.w("loction address", "Canont get Address!")
        }
        return strAdd
    }

    open fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String? {
        var strAdd = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)!!
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Log.w("loction address", strReturnedAddress.toString())
            } else {
                Log.w("loction address", "No Address returned!")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.w("loction address", "Canont get Address!")
        }
        return strAdd
    }

     fun animateMarker(marker: Marker, destination: LatLng, bearing: Float? = null) {
        val startPosition = marker.position
        val endPosition = LatLng(destination.latitude, destination.longitude)
        val latLngInterpolator = LatLngInterpolator.LinearFixed()

        val startRotation = marker.rotation
        var rotationAngle: Float = 0.0F
        if (bearing == null) {
            rotationAngle = getBearing(startPosition, destination)
        } else {
            rotationAngle = bearing
        }

        val valueAnimator = ValueAnimator.ofFloat(0F, 1F)
        valueAnimator.duration = 800 // duration 1 seconds
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { animation ->
            try {
                val v = animation.animatedFraction
                val newPosition = latLngInterpolator.interpolate(
                    v,
                    startPosition,
                    endPosition
                )
                marker.setPosition(newPosition)

                marker.setAnchor(0.5f, 0.5f)
                if (bearing == null) {
                    marker.rotation = computeRotation(v, startRotation, rotationAngle)
                } else {
                    marker.rotation = bearing
                }

            } catch (ex: Exception) {
            }
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }
        })

        valueAnimator.start()
    }

    private fun computeRotation(
        fraction: Float,
        start: Float,
        end: Float
    ): Float {
        val normalizeEnd = end - start // rotate start to 0
        val normalizedEndAbs = (normalizeEnd + 360) % 360

        val direction =
            (if (normalizedEndAbs > 180) -1 else 1).toFloat() // -1 = anticlockwise, 1 = clockwise
        val rotation: Float
        if (direction > 0) {
            rotation = normalizedEndAbs
        } else {
            rotation = normalizedEndAbs - 360
        }

        val result = fraction * rotation + start
        return (result + 360) % 360
    }


    private interface LatLngInterpolator {
        fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng

        class LinearFixed : LatLngInterpolator {
            override fun interpolate(
                fraction: Float,
                a: LatLng,
                b: LatLng
            ): LatLng {
                val lat = (b.latitude - a.latitude) * fraction + a.latitude
                var lngDelta = b.longitude - a.longitude
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360
                }
                val lng = lngDelta * fraction + a.longitude
                return LatLng(lat, lng)
            }
        }
    }


    private fun getBearing(begin: LatLng, end: LatLng): Float {
        val lat = Math.abs(begin.latitude - end.latitude)
        val lng = Math.abs(begin.longitude - end.longitude)

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return Math.toDegrees(Math.atan(lng / lat)).toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 90).toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (Math.toDegrees(Math.atan(lng / lat)) + 180).toFloat()
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 270).toFloat()
        return -1f
    }


    fun addFragment(
        @NonNull fragment: Fragment,
        backStackName: Boolean = false,
        aTAG: String = "",
        popBackStack: Boolean = false,
        isMainFrag: Boolean = false,
        @IdRes containerViewId: Int = R.id.main_content,
    ) {
        /*supportFragmentManager
            .beginTransaction()
            .add(containerViewId, fragment)
            .commit()*/
        hideKeyboard()
        var transition = supportFragmentManager.beginTransaction()
        if (popBackStack) {
            transition.setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_from_left,
                R.anim.slide_in_from_right,
                R.anim.slide_out_from_left
            )
        } else {
            transition.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }

        /*if (backStackName)
            transition.addToBackStack(aTAG)*/
        Log.e("aTAG", "aTAG==>" + aTAG)
        if (backStackName)
            transition.addToBackStack(aTAG)

        if (popBackStack) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        transition.add(containerViewId, fragment, aTAG).commitAllowingStateLoss()

    }

    fun replaceFragment(
        @NonNull fragment: Fragment,
        backStackName: Boolean = false,
        popStack: Boolean = false,
        animation: Boolean = false,
        @IdRes containerViewId: Int = R.id.main_content,
    ) {
        val transition = supportFragmentManager.beginTransaction()
        /* transition.setCustomAnimations(
                 android.R.anim.fade_in,
                 android.R.anim.fade_out,
                 android.R.anim.fade_in,
                 android.R.anim.fade_out
         )*/

        if (animation) {
            transition.setCustomAnimations(
                R.anim.slide_up,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                R.anim.slide_down
            )
        } else {
            transition.setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_from_left,
                R.anim.slide_in_from_left,
                R.anim.slide_out_from_right
            )
        }

        if (popStack)
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        if (backStackName)
            transition.addToBackStack("")

        transition.replace(containerViewId, fragment).commitAllowingStateLoss()
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


    fun callGallery(view: ImageView, callbacks: MyOnItemClickListener) {
        callback = callbacks
        imgView = view
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(
            Intent.createChooser(intent, "Select Image"),
            RESULT_LOAD_IMAGE
        )
    }

    fun roundOfFloat(value: Float): String {
        var valueStr = ""
        val roundOff = (value * 100.0).roundToInt() / 100.0
        valueStr = roundOff.toString()
        return valueStr
    }

    fun roundOfDouble(value: Double): String {
        var valueStr = ""
        val roundOff = (value * 100.0).roundToInt() / 100.0
        valueStr = roundOff.toString()
        return valueStr
    }

/*    fun setPriceWithUnit(price: Double): String {
        var priceStr = ""
        if (checkIfInt(price)) {
            priceStr = String.format(Locale.US, "%.0f", price)
        } else {
            priceStr = String.format(Locale.US, "%.2f", price)
        }
        return repo.appPreference.currency + priceStr
    }*/

    fun setValueWithUnit(price: Double): String {
        var priceStr = ""
        if (checkIfInt(price)) {
            priceStr = String.format(Locale.US, "%.0f", price)
        } else {
            priceStr = String.format(Locale.US, "%.2f", price)
        }
        return priceStr
    }

    open fun checkIfInt(value: Double): Boolean {
        return value - Math.floor(value) == 0.0
    }

    fun callCamera(view: ImageView, callbacks: MyOnItemClickListener) {
        callback = callbacks
        imgView = view
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                Log.e(ContentValues.TAG, "Create Image File")
            } catch (ex: IOException) {
                // Error occurred while creating the File
                Log.e(ContentValues.TAG, "Error File$ex")
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(
                    baseContext, BuildConfig.APPLICATION_ID.toString() + ".provider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            }
            startActivityForResult(
                takePictureIntent,
                Constants.CAMERA_REQUEST
            )
        }
    }

    @Throws(IOException::class)
    fun createImageFile(): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        imgPath = image.absolutePath
        return image
    }

    open fun getRealPathFromUri(context: Context, uri: Uri?): File? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri!!)
            val fileName: String = getFileName(context, uri)!!
            val splitName: Array<String> = splitFileName(fileName)!!
            var tempFile = File.createTempFile(splitName[0], splitName[1])
            tempFile = rename(tempFile, fileName)
            tempFile.deleteOnExit()
            val out = FileOutputStream(tempFile)
            if (inputStream != null) {
                copy(inputStream, out)
                inputStream.close()
            }
            out.close()
            return tempFile
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return null
    }

    public fun meterDistanceBetweenPoints(
        lat_a: Double,
        lng_a: Double,
        lat_b: Double,
        lng_b: Double,
    ): Double {

        val latLngA = LatLng(lat_a, lng_a)
        val latLngB = LatLng(lat_b, lng_b)

        val locationA = Location("point A")
        locationA.latitude = latLngA.latitude;
        locationA.longitude = latLngA.longitude;

        val locationB = Location("point B")
        locationB.latitude = latLngB.latitude
        locationB.longitude = latLngB.longitude
        Log.e("Distance", "Distance between==>" + locationA.distanceTo(locationB))
        return locationA.distanceTo(locationB).toDouble()
    }

    private fun splitFileName(fileName: String): Array<String>? {
        var name = fileName
        var extension = ""
        val i = fileName.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }
        return arrayOf(name, extension)
    }

    private fun rename(file: File, newName: String): File? {
        val newFile = File(file.parent, newName)
        if (newFile != file) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old $newName file")
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to $newName")
            }
        }
        return newFile
    }

    @Throws(IOException::class)
    private fun copy(input: InputStream, output: OutputStream) {
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (EOF != input.read(buffer).also { n = it }) {
            output.write(buffer, 0, n)
        }
    }

    @SuppressLint("Range")
    fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme != null && uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            if (result != null) {
                val cut = result.lastIndexOf(File.separator)
                if (cut != -1) {
                    result = result.substring(cut + 1)
                }
            }
        }
        return result
    }

    open fun compressImage(filePath: String?): String? {
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

//      max Height and width values of the compressed image is taken as 816x612
        val maxHeight = 1024.0f
        val maxWidth = 1024.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

//      width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

//      check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap!!, 0, 0,
                scaledBitmap!!.width, scaledBitmap!!.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val out: FileOutputStream
        val filename: String = getFilename()
        try {
            out = FileOutputStream(filename)

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return filename
    }

    open fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int,
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    open fun getFilename(): String {
        //File file = new File(Environment.getExternalStorageDirectory().getPath(), ".spongy/Images");
        val dir = File(baseContext.getExternalFilesDir("Output").toString())
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val dest = File(
            dir.path + File.separator.toString() + "Output" + System.currentTimeMillis()
                .toString() + ".jpg"
        )
        return dest.absolutePath
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            var uri: Uri
            if (requestCode == Constants.CAMERA_REQUEST) {
                try {
                    if (imageUri != null) {
                        imgPath = getRealPathFromUri(baseContext, imageUri)!!.absolutePath
                        Log.e(ContentValues.TAG, "Camera Path: $imgPath")
                        Glide.with(this)
                            .load(imgPath)
                            .into(imgView!!)
                        val filePath = compressImage(imgPath)
                        callback!!.onActivityResult(filePath!!)
                    } else {
                        Log.e(ContentValues.TAG, "Uri Null")
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    e.localizedMessage
                    Log.e("CAMERA_REQUEST", "Failed to load", e)
                }
            } else if (requestCode == RESULT_LOAD_IMAGE) {
                if (data != null) {
                    uri = data.data!!
                    if (uri != null) {
                        val myFile = File(uri.path)
                        Log.e("uriname=>", "" + uri)
                        imgPath = getRealPathFromUri(baseContext, uri)!!.absolutePath
                        Log.e(ContentValues.TAG, "Gallery Path: $imgPath")
                        Glide.with(this)
                            .load(imgPath)
                            .into(imgView!!)
                        val filePath = compressImage(imgPath)
                        callback!!.onActivityResult(filePath!!)
                    } else {
                        Log.e(ContentValues.TAG, "uri null: ")
                    }
                }
            }
        }
    }


    open fun printLog(message: String) {
        val maxLogSize = 2000
        for (i in 0..message.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = if (end > message.length) message.length else end
            Log.d("finalresponse", "finalresponse==>" + message.substring(start, end))
        }
    }


    inline fun <reified T : Any> Activity.launchActivity(
        requestCode: Int = -1,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {},
    ) {

        val intent = newIntent<T>(this)
        intent.init()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivityForResult(intent, requestCode, options)
        } else {
            startActivityForResult(intent, requestCode)
        }
    }

    inline fun <reified T : Any> newIntent(context: Context): Intent =
        Intent(context, T::class.java)

    val cameraZoomLavel15_0_f = 15.0f
    val cameraZoomLavel18_0_f = 18.0f


}