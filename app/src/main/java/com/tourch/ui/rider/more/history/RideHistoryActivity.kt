package com.tourch.ui.rider.more.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tourch.annotation.Status
import com.tourch.application.Constants
import com.tourch.databinding.ActivityRideHistoryBinding
import com.tourch.retrofit.EncryptedDataResponse
import com.tourch.retrofit.WebResponse
import com.tourch.utils.CommonUtils
import java.util.ArrayList
import java.util.HashMap

class RideHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRideHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel

    private lateinit var rideHistoryAdapter: RideHistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRideHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObserver()
        setToolBar()
        initView()
    }



    private fun setToolBar() {
        binding.toolbar.imgLeftIcon.visibility = View.VISIBLE
        binding.toolbar.tvToolbarTitle.visibility = View.VISIBLE
        binding.toolbar.imgLogo.visibility = View.GONE

        binding.toolbar.tvToolbarTitle.text = "Ride History"

        binding.toolbar.imgLeftIcon.setOnClickListener {
            finish()
        }
    }

    private fun initView() {

        binding.rbAll.setOnClickListener {
            rideHistoryAdapter.filter.filter("")
            rideHistoryAdapter.notifyDataSetChanged()
        }
        binding.rbCompleted.setOnClickListener {
            rideHistoryAdapter.filter.filter("completed")
            rideHistoryAdapter.notifyDataSetChanged()
        }
        binding.rbCancelled.setOnClickListener {
            rideHistoryAdapter.filter.filter("cancelled")
            rideHistoryAdapter.notifyDataSetChanged()
        }

        CommonUtils.showLoading(this)
        val params = HashMap<String, Any>()
        params[Constants.Param.code] = "EN"
        params[Constants.Param.user_id] = "USR167671979346"//arun sir id
        params[Constants.Param.user_type] = "passenger"

        Log.e("TAG", "callOnGoingApi==>" + Gson().toJson(params))
        historyViewModel.callTripHistoryAPI(CommonUtils.encryptData(params))

    }

    private fun initObserver() {
        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        historyViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                CommonUtils.showLoading(this)
            else
                CommonUtils.hideLoading(this)
        }

        historyViewModel.responseTripHistoryData.observe(this) { response: WebResponse<EncryptedDataResponse> ->
            if (response.status == Status.SUCCESS) {
                Toast.makeText(this,"response-> TripHistoryData", Toast.LENGTH_SHORT).show()

                val decrypt = CommonUtils.decryptData(response.data!!.payload.data)
              //  val myType = object : TypeToken<TripDetailsApiResponse>() {}.type

                val myType = object : TypeToken<ArrayList<TripHistoryModel>>() {}.type
                val responseData = Gson().fromJson<ArrayList<TripHistoryModel>>(decrypt, myType)

                Log.e("TAG", "Respose TripHistoryData Api==>" + decrypt)

                showTripHistoryDataAdapter(responseData)


            }
            if (response.status == Status.FAILURE) {
                 Toast.makeText(this, response.errorMsg, Toast.LENGTH_SHORT).show()
                //addFragment(DashboardFragment(), true, "HomeFrag", true, true)
            }
        }

        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        historyViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                CommonUtils.showLoading(this)
            else
                CommonUtils.hideLoading(this)
        }
    }

    private fun showTripHistoryDataAdapter(responseData: ArrayList<TripHistoryModel>) {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvRideHistory.layoutManager = linearLayoutManager

        rideHistoryAdapter = RideHistoryAdapter(responseData, object : RideHistoryAdapter.OnClickRide {
            override fun onClick(position: TripHistoryModel) {
                //fareInfo = position.fareInfo!!
            }
        })
        binding.rvRideHistory.adapter = rideHistoryAdapter
        rideHistoryAdapter.filter.filter("")
        rideHistoryAdapter.notifyDataSetChanged()
    }
}