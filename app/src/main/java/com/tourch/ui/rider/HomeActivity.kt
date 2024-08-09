package com.tourch.ui.rider

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tourch.R
import com.tourch.annotation.Status
import com.tourch.application.Constants
import com.tourch.base.BaseActivity
import com.tourch.databinding.ActivityHomeRiderBinding
import com.tourch.retrofit.EncryptedDataResponse
import com.tourch.retrofit.WebResponse
import com.tourch.ui.rider.model.TripDetailsApiResponse
import com.tourch.utils.CommonUtils
import java.util.HashMap

class HomeActivity : BaseActivity()  {
    private lateinit var binding: ActivityHomeRiderBinding
    private lateinit var riderViewModel: RiderViewModel
    var tripData: TripDetailsApiResponse? = null
    
    private var comeFrom = ""
    private var userRole = ""
    private var countryCode = ""
    private var mobileNo = ""

    companion object {
        lateinit var tvToolbarTitle: TextView
        lateinit var imgMenu: ImageView
        lateinit var imgLeftIcon: ImageView
        lateinit var imgNotification: ImageView
        lateinit var imgaddUser: ImageView
        lateinit var imgLogo: ImageView
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeRiderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentComes = intent
        comeFrom = intentComes.getStringExtra("comeFrom").toString()
        userRole = intentComes.getStringExtra("userRole").toString()
        countryCode = intentComes.getStringExtra("countryCode").toString()
        mobileNo = intentComes.getStringExtra("mobileNo").toString()
        initObserver()
        initUI()

    }

    private fun initUI() {
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle)
        imgMenu = findViewById(R.id.imgMenu)
        imgLeftIcon = findViewById(R.id.imgLeftIcon)
        imgNotification = findViewById(R.id.imgNotification)
        imgaddUser = findViewById(R.id.imgaddUser)
        imgLogo = findViewById(R.id.imgLogo)


        val params = HashMap<String, Any>()
        params[Constants.Param.code] = "EN"
        params[Constants.Param.user_id] = "USR167809928518"//"USR167671979346"
        params[Constants.Param.user_type] = "passenger"

        Log.e("TAG", "callOnGoingApi==>" + Gson().toJson(params))
        riderViewModel.callOngoingTripAPI(CommonUtils.encryptData(params))
    }


    //initialise Observer
    private fun initObserver() {
        riderViewModel = ViewModelProvider(this)[RiderViewModel::class.java]
        riderViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                CommonUtils.showLoading(this)
            else
                CommonUtils.hideLoading(this)
        }

        riderViewModel.responseOngoingData.observe(this) { response: WebResponse<EncryptedDataResponse> ->
            if (response.status == Status.SUCCESS) {
                Toast.makeText(this,"response-> OngoingData", Toast.LENGTH_SHORT).show()

                val decrypt = CommonUtils.decryptData(response.data!!.payload.data)
                val myType = object : TypeToken<TripDetailsApiResponse>() {}.type
                val responseData = Gson().fromJson<TripDetailsApiResponse>(decrypt, myType)

                Log.e("TAG", "Respose Ongoing Api==>" + responseData)
                tripData = responseData

                if (tripData != null){
                    addFragment(RideOngoingFragment("home"), true, "RideOngoingFragment", true, true)
                }else{
                    addFragment(DashboardFragment(), true, "HomeFrag", true, true)
                }

            }
            if (response.status == Status.FAILURE) {
               // Toast.makeText(this, response.errorMsg, Toast.LENGTH_SHORT).show()
                addFragment(DashboardFragment(), true, "HomeFrag", true, true)
            }
        }

        riderViewModel = ViewModelProvider(this)[RiderViewModel::class.java]
        riderViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                CommonUtils.showLoading(this)
            else
                CommonUtils.hideLoading(this)
        }
    }
}