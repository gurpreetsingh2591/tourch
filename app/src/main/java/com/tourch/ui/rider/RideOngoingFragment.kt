package com.tourch.ui.rider

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.PolyUtil
import com.tourch.R
import com.tourch.annotation.Status
import com.tourch.application.Constants
import com.tourch.base.BaseFragment
import com.tourch.databinding.FragmentRideOngoingBinding
import com.tourch.retrofit.ApiService
import com.tourch.retrofit.EncryptedDataResponse
import com.tourch.retrofit.WebResponse
import com.tourch.ui.rider.bottom_dialog.HomeBottomDialog
import com.tourch.ui.rider.model.CancelReasonListApiResponse
import com.tourch.ui.rider.model.TripDetailsApiResponse
import com.tourch.ui.rider.search.model.LocationModel
import com.tourch.utils.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class RideOngoingFragment(var whereFrom: String) : BaseFragment(), View.OnClickListener,
    OnMapReadyCallback {
    private lateinit var binding: FragmentRideOngoingBinding
    var apiCallFirst = true

    var mMap: GoogleMap? = null
    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private lateinit var homeBottomDialog: HomeBottomDialog
    var type = ""

    private lateinit var riderViewModel: RiderViewModel
    var tripData: TripDetailsApiResponse? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    var timer: CountDownTimer? = null

    var trips_data: DatabaseReference? = null
    var driver_data: DatabaseReference? = null
    var tripslistner: ValueEventListener? = null
    var driverlistner: ValueEventListener? = null

    var locationList = ArrayList<TripDetailsApiResponse.DropOff>()
    var dropOffLocations = ArrayList<TripDetailsApiResponse.DropOff>()
    var pickUpLocation = ""
    var pickUpLatitude = 0.0
    var pickUpLongitude = 0.0
    var dropOffLocation = ""
    var dropOffLatitude = 0.0
    var dropOffLongitude = 0.0
    var driverLatitude = 0.0
    var driverLongitude = 0.0
    var driverBearing = ""
    var pickuplatlng: LatLng? = null
    var dropoffLatlng: LatLng? = null

    var tripStatus = ""
    var pickupMarker: Marker? = null
    private var polyLineList: List<LatLng>? = ArrayList<LatLng>()
    internal lateinit var polylineOptions: PolylineOptions
    internal var blackPolyline: Polyline? = null
    internal var greyPolyLine: Polyline? = null
    internal var carmarker: Marker? = null

    internal lateinit var blackPolylineOptions: PolylineOptions
    var strDriverArrivalTime: String = ""
    lateinit var cu: CameraUpdate

    var isInit: Boolean = true
    lateinit var dragState: Any

    companion object {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRideOngoingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MapsInitializer.initialize(
            requireContext(),
            MapsInitializer.Renderer.LATEST
        ) { println(it.name) }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        homeBottomDialog = HomeBottomDialog()
        homeBottomDialog.showSearchVehicle(requireContext())

        val bottomSheet: LinearLayout = view.findViewById(R.id.in_fullDetail)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        dragState = BottomSheetBehavior.STATE_HALF_EXPANDED
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        bottomSheetBehavior.isHideable = false

        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, state: Int) {
                print(state)
                when (state) {

                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        dragState = BottomSheetBehavior.STATE_HALF_EXPANDED
                        layoutSetUpOnBottom("STATE_EXPANDED")
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        dragState = BottomSheetBehavior.STATE_HALF_EXPANDED
                        layoutSetUpOnBottom("STATE_COLLAPSED")
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> {
                        if (dragState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                            layoutSetUpOnBottom("STATE_HALF_EXPANDED")
                        }

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {

                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        dragState = BottomSheetBehavior.STATE_COLLAPSED
                        layoutSetUpOnBottom("STATE_HALF_EXPANDED")
                    }


                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        initObserver(view)
        initView(view)
        placesInitialization()
        setToolBar()

        if (isLocationEnabled()) {
            fetchLocation()
        }
    }


    private fun setToolBar() {
        HomeActivity.imgNotification.visibility = View.GONE
        HomeActivity.imgMenu.visibility = View.GONE
        HomeActivity.imgaddUser.visibility = View.GONE
        HomeActivity.tvToolbarTitle.visibility = View.GONE
    }

    private fun initView(view: View) {
        binding.ivSecurityCenter.setOnClickListener(this)
        callOnGoingApi("firebaseListener")
    }

    private fun callOnGoingApi(s: String) {
        val params = HashMap<String, Any>()
        params[Constants.Param.user_id] = "USR167671979346"
        params[Constants.Param.user_type] = "passenger"

        Log.e("TAG", "callOnGoingApi==>" + Gson().toJson(params))
        riderViewModel.callOngoingTripAPI(CommonUtils.encryptData(params))
    }

    //initialise Observer
    private fun initObserver(view: View) {
        riderViewModel = ViewModelProvider(this)[RiderViewModel::class.java]
        riderViewModel.isLoading.observe(viewLifecycleOwner) { aBoolean ->
            if (aBoolean)
                CommonUtils.showLoading(requireActivity())
            else
                CommonUtils.hideLoading(requireActivity())
        }

        riderViewModel.responseOngoingData.observe(viewLifecycleOwner) { response: WebResponse<EncryptedDataResponse> ->
            if (response.status == Status.SUCCESS) {
                Toast.makeText(requireContext(), "response-> OngoingData", Toast.LENGTH_SHORT).show()

                val decrypt = CommonUtils.decryptData(response.data!!.payload.data)
                val myType = object : TypeToken<TripDetailsApiResponse>() {}.type
                val responseData = Gson().fromJson<TripDetailsApiResponse>(decrypt, myType)

                Log.e("TAG", "Respose Ongoing Api==>" + responseData)
                tripData = responseData

                if (apiCallFirst) {
                    updateQuickUI()
                    apiCallFirst = false
                }

                manageViewByStatus(responseData!!.status)
                if (!tripData!!.driverInfo!!.userId.isNullOrEmpty()) {
                    listenDriverLocation(tripData!!.driverInfo!!.userId!!)
                }
            }
            if (response.status == Status.FAILURE) {
                Toast.makeText(requireContext(), response.errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        riderViewModel.responseCancelReasonData.observe(viewLifecycleOwner) { response: WebResponse<EncryptedDataResponse> ->
            if (response.status == Status.SUCCESS) {
                Toast.makeText(requireContext(), "response-> Cancel reason data", Toast.LENGTH_SHORT).show()

                val decrypt = CommonUtils.decryptData(response.data!!.payload.data)
                val myType = object : TypeToken<ArrayList<CancelReasonListApiResponse.CancelReasonListApiResponseItem>>() {}.type
                val responseData = Gson().fromJson<ArrayList<CancelReasonListApiResponse.CancelReasonListApiResponseItem>>(decrypt, myType)

                Log.e("TAG", "Respose CancelReason Api==>" + responseData)

            }
            if (response.status == Status.FAILURE) {
                Toast.makeText(requireContext(), response.errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        riderViewModel = ViewModelProvider(this)[RiderViewModel::class.java]
        riderViewModel.isLoading.observe(viewLifecycleOwner) { aBoolean ->
            if (aBoolean)
                CommonUtils.showLoading(requireActivity())
            else
                CommonUtils.hideLoading(requireActivity())
        }
    }

    private fun layoutSetUpOnBottom(state: String) {
        if (state == "STATE_EXPANDED") {
            binding.inFullDetail.collapseHalfData.visibility = View.GONE
            binding.inFullDetail.fullDetailsDriver.mainLayoutDD.visibility = View.VISIBLE
        } else {
            binding.inFullDetail.collapseHalfData.visibility = View.VISIBLE
            binding.inFullDetail.fullDetailsDriver.mainLayoutDD.visibility = View.GONE
        }
    }

    private fun placesInitialization() {
        Places.initialize(baseActivity!!, "AIzaSyA0qLYaIHPF1_V4bR4Lh-zf7eFk1YRRdP8")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        val task: Task<Location> = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                val supportMapFragment =
                    (childFragmentManager.findFragmentById(R.id.mapVi) as SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            } else {
                fetchLocation()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationMode = 0
        val locationProviders: String
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            locationMode = try {
                Settings.Secure.getInt(
                    requireActivity().contentResolver,
                    Settings.Secure.LOCATION_MODE
                )
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
                return false
            }
            locationMode != Settings.Secure.LOCATION_MODE_OFF
        } else {
            locationProviders = Settings.Secure.getString(
                requireActivity().contentResolver,
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED
            )
            !TextUtils.isEmpty(locationProviders)
        }
    }

    override fun onClick(p0: View?) {}

    private fun updateQuickUI() {
        Log.e("TAG", "updateUI: ")

        pickUpLatitude = tripData!!.pickUp!!.latitude!!
        pickUpLongitude = tripData!!.pickUp!!.longitude!!
        pickUpLocation = tripData!!.pickUp!!.location!!
        pickuplatlng = LatLng(pickUpLatitude, pickUpLongitude)
        dropOffLocations = tripData!!.dropOff
        dropoffLatlng = LatLng(
            dropOffLocations[dropOffLocations.size - 1].latitude!!,
            dropOffLocations[dropOffLocations.size - 1].longitude!!
        )
        locationList.add(
            TripDetailsApiResponse.DropOff(
                "",
                pickUpLatitude,
                pickUpLocation,
                pickUpLongitude
            )
        )
        locationList.addAll(dropOffLocations)

        manageViewByStatus(tripData!!.status)
        Log.e("TAG", "tripData!!.status==>" + tripData!!.status)
        listenTripsData(tripData!!.tripId!!)
        if (!tripData!!.driverInfo!!.userId.isNullOrEmpty()) {
            listenDriverLocation(tripData!!.driverInfo!!.userId!!)
        }
    }

    private fun manageViewByStatus(status: String?) {
        when (status) {
            "pending" -> {

            }
            else -> {
                when (whereFrom) {
                }
            }
        }
    }


    private fun listenTripsData(tripId: String) {
        Log.e("userId!!", "repo.appPreference.userId!!==>")
        if (trips_data != null && tripslistner != null) {
            trips_data!!.removeEventListener(tripslistner!!)
        }
        if (isInternetConnected()) {
            trips_data = FirebaseDatabase.getInstance().reference.child("trips")
                .child(tripId)

            tripslistner = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.e("firebase", "firebase message ==> " + dataSnapshot.value.toString())
                    val status = dataSnapshot.child("status").value.toString()
                    if (status == "null") {
                        Log.e("Status ==>", "Not found")
                    } else {
                        isInit = true
                        tripStatus = status
                        val driverID = dataSnapshot.child("driver_id").value.toString()
                        Log.e("firebase", "status ==> $status")
                        Log.e("firebase", "driver id ==> $driverID")
                        when (status) {
                            "pending" -> {

                            }
                            "timeout" -> {
                                msgDialog("All drivers are currently occupied; please try again later.")
                                addFragment(DashboardFragment(), true, "HomeFrag", true, true)
                            }
                            "no_service" -> {
                                msgDialog("All drivers are currently occupied; please try again late")
                                addFragment(DashboardFragment(), true, "HomeFrag", true, true)
                            }
                            "accepted" -> {
                                callOnGoingApi("firebaseListener")
                                if (pickupMarker != null) {
                                    pickupMarker!!.remove()
                                }
                                pickupMarker = placeMarkerOnMap(
                                    mMap,
                                    LatLng(pickUpLatitude, pickUpLongitude),
                                    R.drawable.ic_map_pickup
                                )

                                dataSetUpInBottomLay("accepted")
                            }
                            "picked_up" -> {
                                if (pickupMarker != null) {
                                    pickupMarker!!.remove()
                                }
                                pickupMarker = placeMarkerOnMap(
                                    mMap,
                                    LatLng(pickUpLatitude, pickUpLongitude),
                                    R.drawable.ic_map_pickup
                                )
                                dataSetUpInBottomLay("picked_up")
                            }
                            "started" -> {
                                if (pickupMarker != null) {
                                    pickupMarker!!.remove()
                                }
                                pickupMarker = placeMarkerOnMap(
                                    mMap,
                                    dropoffLatlng!!,
                                    R.drawable.ic_map_pickup
                                )
                                dataSetUpInBottomLay("started")
                            }
                            "completed" -> {
                                if (trips_data != null) {
                                    if (tripslistner != null) {
                                        trips_data!!.removeEventListener(tripslistner!!)
                                    }
                                }
                                if (driver_data != null) {
                                    if (driverlistner != null) {
                                        driver_data!!.removeEventListener(driverlistner!!)
                                    }
                                }
                                if (timer != null) {
                                    timer!!.cancel()
                                }
                                when (whereFrom) {
                                    Constants.ServiceCaption.QUICK -> {
                                        // val fragment = RideFinishedFragment()
                                        val bundle = Bundle()
                                        bundle.putString(
                                            "username",
                                            decryptData(tripData!!.driverInfo!!.firstName!!) + " " + decryptData(
                                                tripData!!.driverInfo!!.lastName!!
                                            )
                                        )
                                        Log.e(
                                            "TAG",
                                            "sendVehicleId==>" + tripData!!.vehicleInfo!!.vehicleId
                                        )
                                        bundle.putString(
                                            "vehicleID",
                                            tripData!!.vehicleInfo!!.plateNo
                                        )
                                        bundle.putString(
                                            "photo",
                                            tripData!!.driverInfo!!.photo
                                        )
                                        bundle.putString("trip_id", tripData!!.tripId)
                                        // fragment.arguments = bundle
                                        // addFragment(fragment, true, "RideFinished")
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }
            trips_data!!.addValueEventListener(tripslistner!!)
        }
    }

    private fun dataSetUpInBottomLay(statusRide: String) {
        drawPolylineManageViewByStatus()
        homeBottomDialog.bSSearchVehicleDialog.dismiss()
        binding.ivSecurityCenter.visibility = View.VISIBLE

        if (statusRide == "accepted") {
            binding.inFullDetail.titleRideState.text =
                CommonUtils.decryptData(tripData?.driverInfo?.firstName.toString()) + " is on his way"
            binding.inFullDetail.fullDetailsDriver.titleRideState.text =
                CommonUtils.decryptData(tripData?.driverInfo?.firstName.toString()) + " is on his way"
        } else if (statusRide == "picked_up") {
            binding.inFullDetail.titleRideState.text = "Meet at the pick up point"
            binding.inFullDetail.fullDetailsDriver.titleRideState.text = "Meet at the pick up point"
        } else {
            binding.inFullDetail.titleRideState.text =
                "Your trip is ongoing with " + CommonUtils.decryptData(tripData?.driverInfo?.firstName.toString())
            binding.inFullDetail.fullDetailsDriver.titleRideState.text =
                "Your trip is ongoing with " + CommonUtils.decryptData(tripData?.driverInfo?.firstName.toString())
        }

        //
        binding.inFullDetail.cost.text = "$" + tripData?.fareInfo?.total.toString()
        binding.inFullDetail.code.text = "Code"
        binding.inFullDetail.cabName.text =
            tripData?.vehicleInfo?.model?.title.toString() + "-" + tripData?.vehicleInfo?.plateNo.toString()
        binding.inFullDetail.driverName.text =
            CommonUtils.decryptData(tripData?.driverInfo?.firstName.toString())
        binding.inFullDetail.fullDetailsDriver.cost.text =
            "$" + tripData?.fareInfo?.total.toString()
        binding.inFullDetail.fullDetailsDriver.code.text = "Code"
        binding.inFullDetail.fullDetailsDriver.cabName.text =
            tripData?.vehicleInfo?.model?.title.toString() + "-" + tripData?.vehicleInfo?.plateNo.toString()
        binding.inFullDetail.fullDetailsDriver.driverName.text =
            CommonUtils.decryptData(tripData?.driverInfo?.firstName.toString())
        // binding.inFullDetail.code.text = "$"
        // binding.inFullDetail.code.text = "$"

        binding.inFullDetail.fullDetailsDriver.cancelRideBtn.setOnClickListener {
            val params = HashMap<String, Any>()
            params[Constants.Param.user_id] = "USR167671979346"
            params[Constants.Param.user_type] = "passenger"

            Log.e("TAG", "callOnGoingApi==>" + Gson().toJson(params))
            riderViewModel.callCancelReasonTripAPI(CommonUtils.encryptData(params))
        }


    }

    private fun listenDriverLocation(driverID: String) {
        if (isInternetConnected()) {
            driver_data =
                FirebaseDatabase.getInstance().reference.child("drivers").child(driverID)

            driverlistner = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.e("firebase", "firebase message ==> " + dataSnapshot.value.toString())
                    var data = dataSnapshot.getValue(LocationModel::class.java!!) ?: return
                    driverLatitude = data!!.latitude
                    driverLongitude = data!!.longitude
                    driverBearing = dataSnapshot.child("bearing").value.toString()
                    Log.e("DriverLatitude==>", driverLatitude.toString())
                    Log.e("DriverLongitude==>", driverLongitude.toString())
                    Log.e("tripStatus", "tripStatus" + tripStatus)
                    drawPolylineManageViewByStatus()

                }

                override fun onCancelled(error: DatabaseError) {

                }

            }

            driver_data!!.addValueEventListener(driverlistner!!)
        }
    }

    private fun drawPolylineManageViewByStatus() {
        if (driverLatitude != 0.0 && driverLongitude != 0.0) {
            Log.e("tripStatus", "tripStatus==>" + tripStatus)
            when (tripStatus) {
                "accepted" -> {
                    checkDriverTolerance(
                        LatLng(
                            driverLatitude.toDouble(),
                            driverLongitude.toDouble()
                        ), pickuplatlng!!, false
                    )
                }
                "picked_up" -> {
                    checkDriverTolerance(
                        LatLng(
                            driverLatitude.toDouble(),
                            driverLongitude.toDouble()
                        ), pickuplatlng!!, false
                    )
                }
                "started" -> {
                    checkDriverTolerance(
                        LatLng(
                            driverLatitude.toDouble(),
                            driverLongitude.toDouble()
                        ), dropoffLatlng!!, true
                    )
                }
            }
            updateMap(LatLng(driverLatitude, driverLongitude))
        }
    }

    fun checkDriverTolerance(
        driverLatLong: LatLng,
        dropofflocationlatlong: LatLng,
        addWayPoints: Boolean
    ): Boolean {
        var tolerance = 60.0 // meters
        var route = polyLineList // Your given route
        var isExceededTolerance = false
        // for (l in gpsPoints!!) {
        Log.e(
            "checkDrawPolylineOrNot",
            "distance is = ${PolyUtil.isLocationOnPath(driverLatLong, route, true, tolerance)}"
        )
        Log.e("polyLineList!!.size", "polyLineList!!.size==>" + polyLineList!!.size)
        if (isInit) {
            isInit = false
            drawpoliline(
                driverLatLong,
                dropofflocationlatlong!!, addWayPoints
            )
        } else {
            if (polyLineList!!.size > 0 && PolyUtil.isLocationOnPath(
                    driverLatLong,
                    route,
                    true,
                    tolerance
                )
            ) {
                Log.e("checkDrawPolylineOrNot", "polyLineList.size == 0")
                isExceededTolerance = true

                //break
            } else {
                drawpoliline(
                    driverLatLong,
                    dropofflocationlatlong!!, addWayPoints
                )
            }
        }

        return isExceededTolerance
    }

    fun drawpoliline(pickuplatlng: LatLng, dropofflatlng: LatLng, addWayPoints: Boolean) {
//        runningdriver = false

        var waypoinStr: String? = null
        if (tripData != null) {
            if (tripData!!.dropOff!!.size > 1 && addWayPoints) {
                for (indices in 0 until tripData!!.dropOff!!.size - 1) {
                    if (tripData!!.dropOff!!.get(indices).status.equals("pending")) {
                        waypoinStr += "" + tripData!!.dropOff!![indices].latitude + "," + tripData!!.dropOff!![indices].longitude + "|"
                    }
                }
                if (!waypoinStr.isNullOrEmpty()) {
                    waypoinStr = waypoinStr.substring(0, waypoinStr.length - 1)
                }
            }
        }

        Log.e("DGSdgsdsg", "safsfa")
        Log.e("DGSdgsdsg", "pickuplatlng==>" + pickuplatlng.latitude)
        Log.e("DGSdgsdsg", "pickuplatlng==>" + pickuplatlng.longitude)
        Log.e("DGSdgsdsg", "dropofflatlng==>" + dropofflatlng.latitude)
        Log.e("DGSdgsdsg", "dropofflatlng==>" + dropofflatlng.longitude)

        val apiInterface: ApiService

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://maps.googleapis.com/")
            .build()
        apiInterface = retrofit.create<ApiService>(ApiService::class.java)


        apiInterface.getDirectionsSingle(
            "driving",
            "less_driving",
            //pickuplocation, dropOfflocation,
            pickuplatlng.latitude.toString() + "," + pickuplatlng.longitude,
            dropofflatlng.latitude.toString() + "," + dropofflatlng.longitude,
            if (waypoinStr != null) waypoinStr!!.replace("null", "") else null,
            baseActivity!!.resources.getString(R.string.placekey)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                val routeList = result.getRoutes()
                Log.e("DGsdsgdgsgd", "DGsdsgdgsgd==" + routeList.size)
                Log.e("DGsdsgdgsgd", "DGsdsgdgsgd" + routeList.size)
                if (result.routes.size > 0) {
                    if (result.routes.get(0).legs.size > 0) {
                        strDriverArrivalTime =
                            result.routes.get(0).legs.get(0).duration.text
                        setEtaTimeWithSpan(
                            strDriverArrivalTime,
                            result.routes.get(0).legs.get(0).duration.value.toDouble()
                        )
                        Log.e(
                            "Driver",
                            "Driver Arrival Time" + result.routes.get(0).legs.get(0).duration.text
                        )
                        Log.e(
                            "Driver",
                            "Driver Arrival Time" + result.routes.get(0).legs.get(0).duration.value
                        )
                    }
                }

                for (route in routeList) {
                    val polyLine = route.getOverviewPolyline().getPoints()
                    polyLineList = decodePoly(polyLine)

                    if (greyPolyLine != null) {
                        greyPolyLine!!.remove()
                    }
                    if (blackPolyline != null) {
                        blackPolyline!!.remove()
                    }


                    drawPolyLineAndAnimateCar(pickuplatlng, dropofflatlng)
                }
            }, {
                Log.e("Throwable", "Throwable==" + it.localizedMessage.toString())
                it.printStackTrace()
            })


    }

    private fun setEtaTimeWithSpan(strDriverArrivalTime: String?, value: Double) {
        var text = "Driver is On the Way - Reaching in $strDriverArrivalTime"
        var ss = SpannableString(text)
        var spanText1 = "On the Way"
        var spanText2 = strDriverArrivalTime!!
        var position1 = ((text.indexOf(spanText1)) + (spanText1.length))
        var position2 = ((text.indexOf(spanText2)) + (spanText2.length))
        ss.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(baseActivity!!, R.color.colorPrimary)
            ), text.indexOf(spanText1), position1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(baseActivity!!, R.color.colorPrimary)
            ), text.indexOf(spanText2), position2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        /*  tvEtaTime.setText(ss, TextView.BufferType.SPANNABLE)
          if (value / 60 <= 1) {
              tvEtaTime.text = "Driver has arrived to your location."
          }*/
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }

    private fun drawPolyLineAndAnimateCar(pickuplatlng: LatLng, dropofflatlng: LatLng) {

        val builder = LatLngBounds.Builder()

        for (latLng in polyLineList!!) {
            builder.include(latLng)
            var bounds: LatLngBounds = builder.build()
            cu = CameraUpdateFactory.newLatLngBounds(bounds, 50)
            mMap!!.animateCamera(cu)
        }


        mMap!!.setPadding(
            baseActivity!!.resources.getDimension(com.intuit.sdp.R.dimen._30sdp).toInt(),
            baseActivity!!.resources.getDimension(com.intuit.sdp.R.dimen._25sdp).toInt(),
            baseActivity!!.resources.getDimension(com.intuit.sdp.R.dimen._25sdp).toInt(),
            baseActivity!!.resources.getDimension(com.intuit.sdp.R.dimen._240sdp).toInt()
        )
        //  mMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0))

        polylineOptions = PolylineOptions()
        polylineOptions.color(Color.BLACK)
        polylineOptions.width(10f)
        polylineOptions.startCap(SquareCap())
        polylineOptions.endCap(SquareCap())
        polylineOptions.jointType(JointType.ROUND)
        polylineOptions.addAll(polyLineList!!)
        greyPolyLine = mMap!!.addPolyline(polylineOptions)

        blackPolylineOptions = PolylineOptions()
        blackPolylineOptions.width(10f)
        blackPolylineOptions.color(Color.BLACK)
        blackPolylineOptions.startCap(SquareCap())
        blackPolylineOptions.endCap(SquareCap())
        blackPolylineOptions.jointType(JointType.ROUND)
        blackPolyline = mMap!!.addPolyline(blackPolylineOptions)
//        runningdriver = true


    }

    private fun updateMap(latLng: LatLng) {

        /* map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                latLng, 10f));
        driverMarker = map.addMarker(new MarkerOptions().position(latLng).
                flat(true).
                icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));*/
        if (carmarker != null) {
            Log.e("Caricon", "notnulll")
            animateCar(carmarker!!, latLng)
            val contains = mMap!!.getProjection()
                .visibleRegion
                .latLngBounds
                .contains(latLng)
            if (!contains) {
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            }
            Log.e("CarMarker", "CarMarker==> is not null")
        } else {
            Log.e("CarMarker", "CarMarker==> is null")
            val bitmapdraw = baseActivity!!.resources
                .getDrawable(R.drawable.ic_car_marker) as BitmapDrawable
            val b = bitmapdraw.bitmap
            val smallMarker = Bitmap.createScaledBitmap(
                b,
                baseActivity!!.resources.getDimension(com.intuit.sdp.R.dimen._20sdp).toInt(),
                baseActivity!!.resources.getDimension(com.intuit.sdp.R.dimen._30sdp).toInt(),
                false
            )

            //mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))

            if (mMap != null) {
                Log.e("Caricon", "nullllll")
                carmarker = mMap!!.addMarker(
                    MarkerOptions().position(latLng).flat(true)
                        //                        icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker_homescreen)));
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                )
            }
        }
    }

    private fun animateCar(marker: Marker, destination: LatLng) {
        val startPosition = marker.position
        val endPosition = LatLng(destination.latitude, destination.longitude)
        val latLngInterpolator = LatLngInterpolator.LinearFixed()

        val startRotation = marker.rotation
        val rotationAngle = driverBearing

        val valueAnimator = ValueAnimator.ofFloat(0F, 1F)
        valueAnimator.duration = 800 // duration 1 seconds
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { animation ->
            try {
                val v = animation.animatedFraction
                val newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition)
                marker.position = newPosition

                marker.setAnchor(0.5f, 0.5f)
                marker.rotation = computeRotation(v, startRotation, rotationAngle.toFloat())

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

    private interface LatLngInterpolator {
        fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng

        class LinearFixed : LatLngInterpolator {
            override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
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

    private fun computeRotation(fraction: Float, start: Float, end: Float): Float {
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


}