package com.tourch.ui.rider

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryDataEventListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tourch.R
import com.tourch.annotation.Status
import com.tourch.application.Constants
import com.tourch.base.BaseFragment
import com.tourch.databinding.FragmentDashboardBinding
import com.tourch.databinding.LayoutEnterLocationBinding
import com.tourch.retrofit.ApiService
import com.tourch.retrofit.EncryptedDataResponse
import com.tourch.retrofit.WebResponse
import com.tourch.ui.mapCustomView.DriverTrack
import com.tourch.ui.rider.bottom_dialog.HomeBottomDialog
import com.tourch.ui.rider.model.DropOff
import com.tourch.ui.rider.model.EstimateFareApiResponse
import com.tourch.ui.rider.model.ServiceListApiResponse
import com.tourch.ui.rider.more.MoreActivity
import com.tourch.ui.rider.search.PlacesAutoCompleteAdapter
import com.tourch.ui.rider.search.model.AddMultipleLocationModel
import com.tourch.utils.CommonUtils
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class DashboardFragment : BaseFragment(), View.OnClickListener, OnMapReadyCallback,
    PlacesAutoCompleteAdapter.ClickListener,
    PlacesAutoCompleteAdapter.NoResultListener {
    private lateinit var binding: FragmentDashboardBinding

    private var searchAreaList = ArrayList<String>()
    private var address = ""
    private var latlong = ""
    var mMap: GoogleMap? = null
    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mAutoCompleteAdapter: PlacesAutoCompleteAdapter? = null
    lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var homeBottomDialog: HomeBottomDialog

    lateinit var textInputEditText: TextInputEditText

    var pickup = true
    var gesture: Boolean = false
    var waypoinStr = ""
    private var polyLineList: List<LatLng>? = ArrayList<LatLng>()
    internal var blackPolyline: Polyline? = null
    internal var greyPolyLine: Polyline? = null

    private lateinit var polylineOptions: PolylineOptions
    private lateinit var blackPolylineOptions: PolylineOptions

    lateinit var cu: CameraUpdate

    var pickUpMarker: Marker? = null
    var dropOffMarker: Marker? = null

    var refs: DatabaseReference? = null
    var geoFire: GeoFire? = null
    var geoQuery: GeoQuery? = null
    var geoQueryDataEventListener: GeoQueryDataEventListener? = null
    var markerhasmap = HashMap<String, LatLng>()
    var markerList = HashMap<String, Marker>()
    var markerArrayList = ArrayList<Marker>()
    var m: Marker? = null
    var running = true
    var type = ""


    var services = ArrayList<ServiceListApiResponse.ServiceListApiResponseItem>()
    //val geocoder = Geocoder(requireContext(), Locale.getDefault())

    companion object {
        private lateinit var riderViewModel: RiderViewModel
        var dropOffLocations = ArrayList<DropOff>()
        var dropOffLocationsBook = ArrayList<DropOff>()
        var selectedServiceId = ""
        var pickUpListLocation = ArrayList<AddMultipleLocationModel>()
        var dropOffListLocation = ArrayList<AddMultipleLocationModel>()

        lateinit var tvEnterDesti: TextInputEditText
        lateinit var tilPickUpLay: TextInputLayout
        lateinit var btn_continue: MaterialButton
        lateinit var rl_TvDropOff: RelativeLayout
        var distance: Int = 0

        fun callEstimateFareApi(selectedServiceId: String) {

            if (pickUpListLocation[0].city != dropOffListLocation[0].city || distance > 160934) {

                val params = HashMap<String, Any>()
                params[Constants.Param.code] = "EN"
                params["from_city"] = pickUpListLocation[0].city
                params["to_city"] = dropOffListLocation[0].city
                params["service_id"] = selectedServiceId

                Log.e("TAG", "callEstimateFareApi==>" + Gson().toJson(params))
                riderViewModel.callCityToCityAPI(CommonUtils.encryptData(params))


            } else {

                val params = HashMap<String, Any>()
                var dOff = DropOff()
                dOff.latitude = dropOffListLocation[0].latitude.toDouble()
                dOff.longitude = dropOffListLocation[0].longitude.toDouble()
                dOff.city = dropOffListLocation[0].city

                dropOffLocations.addAll(listOf(dOff))

                params[Constants.Param.code] = "EN"
                params[Constants.Param.user_id] = "USR167671979346"
                params["service_id"] = selectedServiceId
                params["service_type"] = "now"
                params[Constants.Param.pick_up_latitude] = pickUpListLocation[0].latitude
                params[Constants.Param.pick_up_longitude] = pickUpListLocation[0].longitude
                params["pick_up_city"] = pickUpListLocation[0].city
                params[Constants.Param.drop_off] = dropOffLocations
                Log.e("TAG", "callEstimateFareApi==>" + Gson().toJson(params))
                riderViewModel.callEstimateFareAPI(CommonUtils.encryptData(params))
            }
        }

        fun callBookApi(fareInfo: EstimateFareApiResponse.Ride.FareInfo?) {
            val params = HashMap<String, Any>()
            var dOff = DropOff()
            dOff.latitude = dropOffListLocation[0].latitude.toDouble()
            dOff.longitude = dropOffListLocation[0].longitude.toDouble()
            dOff.location = dropOffListLocation[0].adresss
            dOff.city = dropOffListLocation[0].city
            dropOffLocationsBook.addAll(listOf(dOff))

            var fairin = EstimateFareApiResponse.Ride.FareInfo()
            if (fareInfo != null) {
                fairin = fareInfo
            }

            params[Constants.Param.drop_off] = dropOffLocationsBook
            params["fare_info"] = fairin
            params["pick_up_city"] = pickUpListLocation[0].city
            params["type"] = "now"
            params["booking_date"] = "2023-02-28 13:09:17"
            params["vehicle_ride_option_id"] = "VRO167706028183"
            params["service_id"] = selectedServiceId
            params[Constants.Param.user_id] = "USR167671979346"
            params[Constants.Param.pick_up_latitude] = pickUpListLocation[0].latitude
            params[Constants.Param.pick_up_longitude] = pickUpListLocation[0].longitude
            params["pick_up_location"] = pickUpListLocation[0].adresss
            params["is_preferred_female_driver"] = "off"
            params["payment_type"] = "cash"
            params["coupon_id"] = ""

            Log.e("TAG", "callBookingApi==>" + Gson().toJson(params))

            var paramsFin = HashMap<String, Any>()
            paramsFin[Constants.Param.trip_info_json] = params
            riderViewModel.callBookNowTripAPI(CommonUtils.encryptData(paramsFin))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST)
        { println(it.name) }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        homeBottomDialog = HomeBottomDialog()
        initObserver(view)
        initView(view)
        placesInitialization()
        setToolBar()

        if (isLocationEnabled()) {
            fetchLocation()
        }
        riderViewModel.callGetServiceListAPI()
    }

    private fun setToolBar() {
        HomeActivity.imgNotification.visibility = View.VISIBLE
        HomeActivity.imgMenu.visibility = View.VISIBLE
        HomeActivity.imgaddUser.visibility = View.GONE
        HomeActivity.tvToolbarTitle.visibility = View.GONE
    }

    private fun initView(view: View) {
        searchAreaList.add("1/2 miles")
        searchAreaList.add("3/4 miles")
        searchAreaList.add("1 miles")
        searchAreaList.add("1.5 miles")
        searchAreaList.add("2 miles")
        searchAreaList.add("3 miles")
        searchAreaList.add("custom")

        tvEnterDesti = view.findViewById(R.id.tvEnterDestination)
        btn_continue = view.findViewById(R.id.btnContinue)
        rl_TvDropOff = view.findViewById(R.id.rlTvDropOff)
        tilPickUpLay = view.findViewById(R.id.tilPickUp)

        binding.ivSecurityCenter.setOnClickListener(this)


        binding.inDestination.tvEnterDestination.setOnClickListener {
            showBSDEnterLoc()
        }

        binding.inDestination.tvBack.setOnClickListener {
            binding.inDestination.tvBack.visibility = View.GONE
            binding.inDestination.rlTvDropOff.visibility = View.GONE
            btn_continue.visibility = View.GONE
            binding.inDestination.tilPickUp.hint =
                resources.getString(R.string.enter_your_destination)
            binding.inDestination.tvEnterDestination.text?.clear()
        }

        btn_continue.setOnClickListener {
            if (services.size != 0) {
                selectedServiceId = services[0].serviceId.toString()
            } else {
                return@setOnClickListener
            }
            callEstimateFareApi(selectedServiceId)
        }

        binding.ivMore.setOnClickListener {
            startActivity(Intent(requireContext(), MoreActivity::class.java))
        }
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


        riderViewModel.responseServiceData.observe(viewLifecycleOwner) { response: WebResponse<EncryptedDataResponse> ->
            if (response.status == Status.SUCCESS) {
                Toast.makeText(requireContext(), "responseServiceData", Toast.LENGTH_SHORT).show()
                val decrypt = CommonUtils.decryptData(response.data!!.payload.data)

                val myType = object : TypeToken<ArrayList<ServiceListApiResponse.ServiceListApiResponseItem>>() {}.type
                val responseData =
                    Gson().fromJson<ArrayList<ServiceListApiResponse.ServiceListApiResponseItem>>(
                        decrypt,
                        myType
                    )
                services.addAll(responseData)
                Log.e("TAG", "Respose Service List Api==>" + responseData)
            }
            if (response.status == Status.FAILURE) {
                Toast.makeText(requireContext(), response.errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        riderViewModel.responseCityToCityData.observe(viewLifecycleOwner) { response: WebResponse<EncryptedDataResponse> ->
            if (response.status == Status.SUCCESS) {
                Toast.makeText(requireContext(), "responseCityToCityData", Toast.LENGTH_SHORT).show()
                val decrypt = CommonUtils.decryptData(response.data!!.payload.data)

                /*val myType = object :
                    TypeToken<ArrayList<ServiceListApiResponse.ServiceListApiResponseItem>>() {}.type
                val responseData =
                    Gson().fromJson<ArrayList<ServiceListApiResponse.ServiceListApiResponseItem>>(
                        decrypt,
                        myType
                    )
                services.addAll(responseData)*/
                Log.e("TAG", "Respose responseCityToCityData Api==>" + decrypt)
            }
            if (response.status == Status.FAILURE) {
                Toast.makeText(requireContext(), response.errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        riderViewModel.responseEstimateFareData.observe(viewLifecycleOwner) { response: WebResponse<EncryptedDataResponse> ->
            if (response.status == Status.SUCCESS) {
                Toast.makeText(requireContext(), "responseEstimateFareData", Toast.LENGTH_SHORT)
                    .show()

                val decrypt = CommonUtils.decryptData(response.data!!.payload.data)
                val myType = object : TypeToken<EstimateFareApiResponse>() {}.type
                val responseData = Gson().fromJson<EstimateFareApiResponse>(decrypt, myType)

                Log.e("TAG", "Respose EstimateFareApi==>" + responseData)

                val rides: ArrayList<EstimateFareApiResponse.Ride> = arrayListOf()

                for (i in 0 until responseData.rides.size) {
                    if (responseData.rides[i].vehicleDuration?.durationValue != null)
                        rides.add(responseData.rides[i])
                }

                homeBottomDialog.showSelectVehicle(requireContext(), view, rides, services)

            }
            if (response.status == Status.FAILURE) {
                Toast.makeText(requireContext(), response.errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        riderViewModel.responseBookingData.observe(viewLifecycleOwner) { response: WebResponse<EncryptedDataResponse> ->
            if (response.status == Status.SUCCESS) {
                Toast.makeText(requireContext(), "responseBookingData", Toast.LENGTH_SHORT).show()

                val decrypt = CommonUtils.decryptData(response.data!!.payload.data)
                // val myType = object : TypeToken<EstimateFareApiResponse>() {}.type
                // val responseData = Gson().fromJson<EstimateFareApiResponse>(decrypt, myType)

                /*             Log.e("TAG", "Respose BookingData Api==>" + decrypt)

                             val rides: ArrayList<EstimateFareApiResponse.Ride> = arrayListOf()

                             for (i in 0 until responseData.rides.size) {
                                 if (responseData.rides[i].vehicleDuration?.durationValue != null)
                                     rides.add(responseData.rides[i])
                             }

                             homeBottomDialog.showSelectVehicle(requireContext(), view, rides, services)*/

                val fragment = RideOngoingFragment(Constants.ServiceCaption.QUICK)
                val data = Gson().toJson(decrypt)
                val bundle = Bundle()
                bundle.putString("data", data)
                fragment.arguments = bundle
                addFragment(fragment, true, "RideOngoingFragment")
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

    private fun placesInitialization() {
        Places.initialize(baseActivity!!, "AIzaSyA0qLYaIHPF1_V4bR4Lh-zf7eFk1YRRdP8")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap!!.isMyLocationEnabled = true

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
            LatLng(
                currentLocation!!.latitude,
                currentLocation!!.longitude
            ), 14f
        )
        googleMap.animateCamera(cameraUpdate)
        pickUpMarker = placeMarkerOnMap(
            mMap,
            LatLng(currentLocation!!.latitude, currentLocation!!.longitude),
            R.drawable.marker
        )

        mMap!!.setOnMapLoadedCallback {
            subscribeToUpdates(currentLocation!!.latitude, currentLocation!!.longitude)
        }
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

    override fun click(place: Place?) {
        if (place != null) {
            textInputEditText.setText(place.address!!.toString())

            address = place.address!!.toString()
            latlong = place.latLng!!.toString()
            var strMain = latlong.toString()
            /*-----Latlong separate from string-----*/
            strMain = strMain.replace("lat/lng: (", "")
            strMain = strMain.replace(")", "")
            val strLatLong = strMain.split("\\|").toTypedArray()
            var latitude = 0.0
            var longitude = 0.0
            for (item in strLatLong) {
                val str = item.split(",").toTypedArray()
                latitude = str[0].toDouble()
                longitude = str[1].toDouble()
            }

            Log.d("Address", address)
            Log.d("Address latitude", latitude.toString())
            Log.d("Address longitude", longitude.toString())
            Log.d("Address City", cityFromLatLong(latitude, longitude))

            if (pickup) {
                val addLocationPic = AddMultipleLocationModel()
                addLocationPic.adresss = address
                addLocationPic.latitude = latitude.toString()
                addLocationPic.longitude = longitude.toString()
                addLocationPic.city = cityFromLatLong(latitude, longitude)
                pickUpListLocation.add(0, addLocationPic)

                if (pickUpMarker != null) {
                    pickUpMarker!!.remove()
                }
                pickUpMarker = placeMarkerOnMap(
                    mMap,
                    LatLng(
                        pickUpListLocation[0].latitude.toDouble(),
                        pickUpListLocation[0].longitude.toDouble()
                    ),
                    R.drawable.pick_marker
                )


            } else {
                val addLocationDoff = AddMultipleLocationModel()
                addLocationDoff.adresss = address
                addLocationDoff.latitude = latitude.toString()
                addLocationDoff.longitude = longitude.toString()
                addLocationDoff.city = cityFromLatLong(latitude, longitude)
                dropOffListLocation.add(0, addLocationDoff)

                if (dropOffMarker != null) {
                    dropOffMarker!!.remove()
                }
                dropOffMarker = placeMarkerOnMap(
                    mMap,
                    LatLng(
                        dropOffListLocation[0].latitude.toDouble(),
                        dropOffListLocation[0].longitude.toDouble()
                    ),
                    R.drawable.drop_marker
                )
            }
        }
    }

    override fun noresult(integer: Int?) {}

    private fun showBSDEnterLoc() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val bindingLoc: LayoutEnterLocationBinding = LayoutEnterLocationBinding.inflate(
            bottomSheetDialog.layoutInflater,
            binding.root,
            false
        )
        bottomSheetDialog.setContentView(bindingLoc.root)

        val behavior = bottomSheetDialog.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        setPlacesAdapter(bindingLoc)


        if (bindingLoc.edPickUp.text.toString() == "Your Location") {
            bindingLoc.rlYourLoc.visibility = View.GONE
        }

        bindingLoc.rlYourLoc.setOnClickListener {
            bindingLoc.edPickUp.setText("Your Location")
            bindingLoc.rlYourLoc.visibility = View.GONE
        }

        bindingLoc.ivBack.setOnClickListener {
            bottomSheetDialog.dismiss()
            binding.inDestination.tvDropOff.visibility = View.GONE
            btn_continue.visibility = View.GONE
        }
        bindingLoc.tvDoneDialog.setOnClickListener {
            if (bindingLoc.edPickUp.text.toString()
                    .isNotEmpty() && bindingLoc.edDropOff.text.toString().isNotEmpty()
            ) {
                btn_continue.visibility = View.VISIBLE
                rl_TvDropOff.visibility = View.VISIBLE
                binding.inDestination.tvBack.visibility = View.VISIBLE
                tvEnterDesti.setText(bindingLoc.edPickUp.text.toString())
                binding.inDestination.tilPickUp.hint = "Pick Off"
                binding.inDestination.tvDropOff.setText(bindingLoc.edDropOff.text.toString())
                bottomSheetDialog.dismiss()

                if (bindingLoc.edPickUp.text.toString() == "Your Location") {
                    if (pickUpMarker != null) {
                        pickUpMarker!!.remove()
                    }
                    pickUpMarker = placeMarkerOnMap(
                        mMap,
                        LatLng(currentLocation!!.latitude, currentLocation!!.longitude),
                        R.drawable.pick_marker
                    )

                    val addLocationPic = AddMultipleLocationModel()
                    addLocationPic.adresss =
                        addressFromLatLong(currentLocation!!.latitude, currentLocation!!.longitude)
                    addLocationPic.latitude = currentLocation!!.latitude.toString()
                    addLocationPic.longitude = currentLocation!!.longitude.toString()
                    addLocationPic.city =
                        cityFromLatLong(currentLocation!!.latitude, currentLocation!!.longitude)
                    pickUpListLocation.add(0, addLocationPic)


                    drawpoliline(
                        currentLocation!!.latitude.toString(),
                        currentLocation!!.longitude.toString(),
                        dropOffListLocation[0].latitude,
                        dropOffListLocation[0].longitude
                    )
                } else {
                    drawpoliline(
                        pickUpListLocation[0].latitude,
                        pickUpListLocation[0].longitude,
                        dropOffListLocation[0].latitude,
                        dropOffListLocation[0].longitude
                    )

                    Log.d("Address pickUpListLocation", pickUpListLocation.toString())
                    Log.d("Address dropOffListLocation", dropOffListLocation.toString())
                }
            } else {
                showToast("Please Enter pickup and drop off locations")
            }
        }

        bindingLoc.ivStopVisible.setOnClickListener {
            if (!bindingLoc.rlDropOff2.isVisible) {
                bindingLoc.tilOne.hint = "Stop 1"
                bindingLoc.rlDropOff2.visibility = View.VISIBLE
            } else if (!bindingLoc.rlDropOff3.isVisible) {
                bindingLoc.rlDropOff3.visibility = View.VISIBLE
            } else if (!bindingLoc.rlDropOff4.isVisible) {
                bindingLoc.rlDropOff4.visibility = View.VISIBLE
            } else if (!bindingLoc.rlDropOff5.isVisible) {
                bindingLoc.rlDropOff5.visibility = View.VISIBLE
            }
        }

        bindingLoc.ivStopGone2.setOnClickListener {
            if (bindingLoc.rlDropOff5.isVisible) {
                bindingLoc.rlDropOff5.visibility = View.GONE
            } else if (bindingLoc.rlDropOff4.isVisible) {
                bindingLoc.rlDropOff4.visibility = View.GONE
            } else if (bindingLoc.rlDropOff3.isVisible) {
                bindingLoc.rlDropOff3.visibility = View.GONE
            } else {
                bindingLoc.tilOne.hint = "Drop off"
                bindingLoc.rlDropOff2.visibility = View.GONE
            }
        }
        bindingLoc.ivStopGone3.setOnClickListener {
            if (bindingLoc.rlDropOff5.isVisible) {
                bindingLoc.rlDropOff5.visibility = View.GONE
            } else if (bindingLoc.rlDropOff4.isVisible) {
                bindingLoc.rlDropOff4.visibility = View.GONE
            } else {
                bindingLoc.rlDropOff3.visibility = View.GONE
            }
        }
        bindingLoc.ivStopGone4.setOnClickListener {
            if (bindingLoc.rlDropOff5.isVisible) {
                bindingLoc.rlDropOff5.visibility = View.GONE
            } else {
                bindingLoc.rlDropOff4.visibility = View.GONE
            }
        }
        bindingLoc.ivStopGone5.setOnClickListener {
            bindingLoc.rlDropOff5.visibility = View.GONE
        }


        bindingLoc.edPickUp.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                Log.d("ADD STOP", "afterTextChanged  $s")
                pickup = true
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                textInputEditText = bindingLoc.edPickUp
                Log.d("ADD STOP", "beforeTextChanged  $s")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                Log.d("ADD STOP", "onTextChanged  $s")

                if (mAutoCompleteAdapter != null) {
                    mAutoCompleteAdapter!!.filter.filter(s.toString())
                    val boolean = false
                    if (boolean) {
                        bindingLoc.edPickUp.clearFocus()
                    } else {
                    }
                }
                if (bindingLoc.edPickUp.text!!.isNotEmpty()) {
                    bindingLoc.rvPlaces.visibility = View.VISIBLE
                    bindingLoc.rlYourLoc.visibility = View.VISIBLE
                } else {
                    bindingLoc.rvPlaces.visibility = View.GONE
                }
            }
        })
        bindingLoc.edDropOff.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                Log.d("ADD STOP", "afterTextChanged  $s")
                pickup = false
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                textInputEditText = bindingLoc.edDropOff
                Log.d("ADD STOP", "beforeTextChanged  $s")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                Log.d("ADD STOP", "onTextChanged  $s")

                if (mAutoCompleteAdapter != null) {
                    mAutoCompleteAdapter!!.filter.filter(s.toString())
                    val boolean = false
                    if (boolean) {
                        bindingLoc.edDropOff.clearFocus()
                    } else {
                    }
                }
                if (bindingLoc.edDropOff.text!!.isNotEmpty()) {
                    bindingLoc.rvPlaces.visibility = View.VISIBLE
                } else {
                    bindingLoc.rvPlaces.visibility = View.GONE
                }
            }
        })

        bindingLoc.edDropOff2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                Log.d("ADD STOP", "afterTextChanged  $s")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                textInputEditText = bindingLoc.edDropOff2
                Log.d("ADD STOP", "beforeTextChanged  $s")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("ADD STOP", "onTextChanged  $s")

                if (mAutoCompleteAdapter != null) {
                    mAutoCompleteAdapter!!.filter.filter(s.toString())
                    val boolean = false
                    if (boolean) {
                        bindingLoc.edDropOff2.clearFocus()
                    } else {
                    }
                }
                if (bindingLoc.edDropOff2.text!!.isNotEmpty()) {
                    bindingLoc.rvPlaces.visibility = View.VISIBLE
                } else {
                    bindingLoc.rvPlaces.visibility = View.GONE
                }
            }
        })
        bindingLoc.edDropOff3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                Log.d("ADD STOP", "afterTextChanged  $s")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                textInputEditText = bindingLoc.edDropOff3
                Log.d("ADD STOP", "beforeTextChanged  $s")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                Log.d("ADD STOP", "onTextChanged  $s")

                if (mAutoCompleteAdapter != null) {
                    mAutoCompleteAdapter!!.filter.filter(s.toString())
                    val boolean = false
                    if (boolean) {
                        bindingLoc.edDropOff3.clearFocus()
                    } else {
                    }
                }
                if (bindingLoc.edDropOff3.text!!.isNotEmpty()) {
                    bindingLoc.rvPlaces.visibility = View.VISIBLE
                } else {
                    bindingLoc.rvPlaces.visibility = View.GONE
                }

            }
        })
        bindingLoc.edDropOff4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                Log.d("ADD STOP", "afterTextChanged  $s")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                textInputEditText = bindingLoc.edDropOff4
                Log.d("ADD STOP", "beforeTextChanged  $s")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("ADD STOP", "onTextChanged  $s")
                if (mAutoCompleteAdapter != null) {
                    mAutoCompleteAdapter!!.filter.filter(s.toString())
                    val boolean = false
                    if (boolean) {
                        bindingLoc.edDropOff4.clearFocus()
                    } else {
                    }
                }
                if (bindingLoc.edDropOff4.text!!.isNotEmpty()) {
                    bindingLoc.rvPlaces.visibility = View.VISIBLE
                } else {
                    bindingLoc.rvPlaces.visibility = View.GONE
                }

            }
        })
        bindingLoc.edDropOff5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                Log.d("ADD STOP", "afterTextChanged  $s")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                textInputEditText = bindingLoc.edDropOff5
                Log.d("ADD STOP", "beforeTextChanged  $s")
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("ADD STOP", "onTextChanged  $s")
                if (mAutoCompleteAdapter != null) {
                    mAutoCompleteAdapter!!.filter.filter(s.toString())
                    val boolean = false
                    if (boolean) {
                        bindingLoc.edDropOff5.clearFocus()
                    } else {
                    }
                }
                if (bindingLoc.edDropOff5.text!!.isNotEmpty()) {
                    bindingLoc.rvPlaces.visibility = View.VISIBLE
                } else {
                    bindingLoc.rvPlaces.visibility = View.GONE
                }
            }
        })
        bottomSheetDialog.show()
    }

    private fun setPlacesAdapter(bindingLoc: LayoutEnterLocationBinding) {
        bindingLoc.rvPlaces.layoutManager =
            LinearLayoutManager(baseActivity!!, LinearLayoutManager.VERTICAL, false)

        mAutoCompleteAdapter = PlacesAutoCompleteAdapter(requireActivity())
        bindingLoc.rvPlaces.adapter = mAutoCompleteAdapter

        mAutoCompleteAdapter!!.setClickListener(this)
        mAutoCompleteAdapter!!.setNoResultListener(this)
        mAutoCompleteAdapter!!.notifyDataSetChanged()
    }

    fun drawpoliline(
        pickuplat: String,
        pickuplong: String,
        dropofflat: String,
        dropofflong: String,
    ) {
//        runningdriver = false

        Log.e("DGSdgsdsg", "safsfa")
        Log.e("DGSdgsdsg", "pickuplatlng==>" + pickuplat)
        Log.e("DGSdgsdsg", "pickuplatlng==>" + pickuplong)
        Log.e("DGSdgsdsg", "dropofflatlng==>" + dropofflat)
        Log.e("DGSdgsdsg", "dropofflatlng==>" + dropofflong)
        Log.e("waypoints", "waypoints==>" + waypoinStr)

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
            "$pickuplat,$pickuplong",
            "$dropofflat,$dropofflong",
            waypoinStr,
            "AIzaSyA0qLYaIHPF1_V4bR4Lh-zf7eFk1YRRdP8"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object :
                    SingleObserver<com.tourch.ui.mapCustomView.map_poliline.Result> {

                    override fun onSubscribe(d: Disposable) {}

                    override fun onSuccess(result: com.tourch.ui.mapCustomView.map_poliline.Result) {
                        Log.e("Result ==>", "" + result)
                        val routeList = result.routes
                        Log.e("DGsdsgdgsgd", "" + routeList.size)
                        if (result.routes.size > 0) {
                            for (route in routeList) {
                                val polyLine = route.overviewPolyline.points

                                distance = route.legs[0].distance.value

                                Log.e("distance ==>", "" + route.legs[0].distance.value)

                                polyLineList = decodePoly(polyLine)

                                if (greyPolyLine != null) {
                                    greyPolyLine!!.remove()
                                }
                                if (blackPolyline != null) {
                                    blackPolyline!!.remove()
                                }
                                drawPolyLineAndAnimateCar(
                                    LatLng(
                                        pickuplat.toDouble(),
                                        pickuplong.toDouble()
                                    ),
                                    LatLng(dropofflat.toDouble(), dropofflong.toDouble())
                                )
                            }

                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        Log.e("dsgdgdsgdsgdsgg", e.message!!)
                    }
                })

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


        /* if (_map != null) {
            _map.clear();
        }*/

        if (!gesture) {
            val builder = LatLngBounds.Builder()
            for (latLng in polyLineList!!) {
                builder.include(latLng)
                val bounds: LatLngBounds = builder.build()
                cu = CameraUpdateFactory.newLatLngBounds(bounds, 50)
                mMap!!.animateCamera(cu)
            }


        }

        polylineOptions = PolylineOptions()
        polylineOptions.color(Color.BLACK)
        polylineOptions.width(8f)
        polylineOptions.startCap(SquareCap())
        polylineOptions.endCap(SquareCap())
        polylineOptions.jointType(JointType.ROUND)
        polylineOptions.addAll(polyLineList!!)
        greyPolyLine = mMap!!.addPolyline(polylineOptions)

        blackPolylineOptions = PolylineOptions()
        blackPolylineOptions.width(8f)
        blackPolylineOptions.color(Color.BLACK)
        blackPolylineOptions.startCap(SquareCap())
        blackPolylineOptions.endCap(SquareCap())
        blackPolylineOptions.jointType(JointType.ROUND)
        blackPolyline = mMap!!.addPolyline(blackPolylineOptions)
//        runningdriver = true

    }

    private fun subscribeToUpdates(slat: Double, slongs: Double) {
        if (slat != 0.0 && slongs != 0.0 && !TextUtils.isEmpty("" + slat) && !TextUtils.isEmpty("" + slongs)) {
            Log.e("subscribeToUpdates", "slat==>$slat")
            Log.e("subscribeToUpdates", "slongs==>$slongs")

            if (refs == null) {
                Log.e("subscribeToUpdates", "null")
            } else {
                Log.e("subscribeToUpdates", "notnulll")
            }
            refs = FirebaseDatabase.getInstance().getReference("drivers")
            geoFire = GeoFire(refs)

//            geoQuery = geoFire.queryAtLocation(new GeoLocation(slat, slongs), Double.parseDouble(Utility.getStringSharedPreferences(getActivity(), ConstantStore.RADIUS)));
            geoQuery = geoFire!!.queryAtLocation(GeoLocation(slat, slongs), 500.00)
            geoQueryDataEventListener = object : GeoQueryDataEventListener {
                override fun onDataEntered(dataSnapshot: DataSnapshot?, location: GeoLocation?) {
                    if (dataSnapshot == null || location == null)
                        return

                    if (location.longitude > 0 && location.longitude > 0) {
                        Log.e("GDSuggests", dataSnapshot.toString())
                        val latLng = LatLng(location.latitude, location.longitude)
                        val driverTrack: DriverTrack =
                            dataSnapshot.getValue(DriverTrack::class.java)!!
                        if (!TextUtils.isEmpty(driverTrack.status)) {
                            if (driverTrack.status.equals("online")) {
                                markerhasmap[dataSnapshot.key!!] = latLng
                            }
                        }
                        //addDestinationIconSymbolLayer(key,location);
                        if (markerhasmap[dataSnapshot.key] != null) {
                            for (i in 0 until markerhasmap.size) {
                                Log.e("getDriverLocation", dataSnapshot.toString())
                                if (markerList[dataSnapshot.key] == null) {
                                    var bitmapdraw: BitmapDrawable = baseActivity!!.resources
                                        .getDrawable(R.drawable.ic_car_marker) as BitmapDrawable


                                    val b = bitmapdraw.bitmap
                                    val smallMarker = Bitmap.createScaledBitmap(
                                        b,
                                        baseActivity!!.resources.getDimension(com.intuit.sdp.R.dimen._20sdp)
                                            .toInt(),
                                        baseActivity!!.resources.getDimension(com.intuit.sdp.R.dimen._30sdp)
                                            .toInt(),
                                        false
                                    )

                                    val markerOptions = MarkerOptions()
                                    markerOptions.position(
                                        LatLng(
                                            location.latitude,
                                            location.longitude
                                        )
                                    )
                                    markerOptions.icon(
                                        BitmapDescriptorFactory.fromBitmap(
                                            smallMarker
                                        )
                                    )
                                    markerOptions.flat(true)
                                    m = mMap!!.addMarker(markerOptions)
                                    markerArrayList.add(m!!)
                                    markerList[dataSnapshot.key!!] = m!!
                                }
                            }
                        }
                    }
                }

                override fun onDataExited(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot != null) {
                        Log.e("subscribeToUpdates", "onDataExited => " + dataSnapshot.key)
                        if (markerList.get(dataSnapshot.key) != null) {
                            m = markerList.get(dataSnapshot.key)
                            m!!.remove()
                            markerList.remove(dataSnapshot.key)
                        }
                    }
                }

                override fun onDataMoved(dataSnapshot: DataSnapshot?, location: GeoLocation?) {
                }

                override fun onDataChanged(dataSnapshot: DataSnapshot, location: GeoLocation?) {
                    // ...
                    try {


                        if (dataSnapshot == null || location == null)
                            return

                        val data: DriverTrack? = dataSnapshot.getValue(
                            DriverTrack::class.java
                        )
                        Log.e("subscribeToUpdates", "running==>$running")
                        if (data == null)
                            return

                        if (!running) {

                            geoQuery!!.removeGeoQueryEventListener(this)
                        }
                        if (data == null)
                            return
                        if (dataSnapshot == null)
                            return

                        Log.e("FSDfsdfsdfsf", "running==>$running")

                        if (data.latitude != null && data!!.longitude != null) {
                            val marker = markerList[dataSnapshot.key]
                            //                    LatLng oldPos = marker.getPosition();
                            val newpos = LatLng(data.latitude, data.longitude)
                            Log.e("getModelId", "" + data.latitude)
                            val loc = Location("update")
                            loc.latitude = newpos.latitude
                            loc.longitude = newpos.longitude

//                            animateMarkerNew(loc,marker);

                            if (data.status.equals("online")) {
                                if (markerList[dataSnapshot.key] == null) {
                                    if (!TextUtils.isEmpty(data.status)) {

                                        var bitmapdraw = baseActivity!!.resources
                                            .getDrawable(R.drawable.ic_car_marker) as BitmapDrawable
                                        val b = bitmapdraw.bitmap
                                        val smallMarker = Bitmap.createScaledBitmap(
                                            b,
                                            baseActivity!!.resources.getDimension(com.intuit.sdp.R.dimen._20sdp)
                                                .toInt(),
                                            baseActivity!!.resources.getDimension(com.intuit.sdp.R.dimen._30sdp)
                                                .toInt(),
                                            false
                                        )

                                        val markerOptions = MarkerOptions()
                                        markerOptions.position(
                                            LatLng(
                                                location.latitude,
                                                location.longitude
                                            )
                                        )
                                        markerOptions.icon(
                                            BitmapDescriptorFactory.fromBitmap(
                                                smallMarker
                                            )
                                        )
                                        markerOptions.flat(true)
                                        m = mMap!!.addMarker(markerOptions)
                                        markerArrayList.add(m!!)
                                        markerList[dataSnapshot.key!!] = m!!
                                    }
                                }
                            } else {
                                updateUI(newpos, dataSnapshot.key!!, data.bearing)
                            }
                        } else {
                            if (markerList[dataSnapshot.key] != null) {
                                m = markerList[dataSnapshot.key]
                                markerArrayList.remove(m!!)
                                m!!.remove()
                                markerList.remove(dataSnapshot.key)
                            }
                        }

                    } catch (e: Exception) {
                        Log.e("Exception", "Exceptionassd==>" + e.localizedMessage!!.toString())
                    }
                }

                override fun onGeoQueryReady() {
                }

                override fun onGeoQueryError(error: DatabaseError?) {
                }
            }
            geoQuery!!.addGeoQueryDataEventListener(geoQueryDataEventListener)
            markerhasmap.keys
        }
    }

    private fun updateUI(latLng: LatLng, key: String, bearing: Float) {
        var driverMarker = markerList[key]
        if (driverMarker != null) {
            baseActivity!!.animateMarker(driverMarker, latLng, bearing)
            val contains: Boolean = mMap!!.projection
                .visibleRegion.latLngBounds
                .contains(latLng)
        } else {

            val bitmapdraw =
                resources.getDrawable(R.drawable.ic_car_marker) as BitmapDrawable
            val b = bitmapdraw.bitmap

            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))


            driverMarker = baseActivity!!.placeMarkerOnMapWithSize(
                mMap!!,
                m,
                latLng,
                R.drawable.ic_car_marker,
                baseActivity!!.resources.getDimension(com.intuit.sdp.R.dimen._30sdp).toInt(),
                baseActivity!!.resources.getDimension(com.intuit.sdp.R.dimen._20sdp).toInt(),
                false,
                15.0F
            )
        }
    }

}