package com.tourch.ui.driver.verification.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tourch.R
import com.tourch.databinding.ActivityDriverHomeVerifiationBinding
import com.tourch.databinding.ActivityDriverInfoBinding
import com.tourch.ui.driver.verification.adapter.HomeVerificationAdapter
import com.tourch.ui.driver.verification.adapter.StepsAdapter
import com.tourch.ui.driver.verification.model.HomeVerificationModel

class DriverInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDriverInfoBinding
    private lateinit var stepsAdapter: StepsAdapter
    private var verificationList: ArrayList<HomeVerificationModel> = arrayListOf()
    private var typeList: ArrayList<String> = arrayListOf()
    private var completionList: ArrayList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()
    }

    private fun initView() {
        typeList.add("Driver Information")
        typeList.add("Driver Licence")
        typeList.add("Vehicle Information")
        typeList.add("Driver Consent")
        typeList.add("Document Upload Centre")
        typeList.add("Tourch Values")
        completionList.add(20)
        completionList.add(0)
        completionList.add(10)
        completionList.add(0)
        completionList.add(0)
        completionList.add(0)

        for (i in typeList.indices) {
            val homeVerificationModel = HomeVerificationModel(
                "Step" + (i + 1) + ": ",
                typeList[i],
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit ut aliquam.",
                "pending",
                completionList[i]
            )

            verificationList.add(homeVerificationModel)
        }

        initRv()

        binding.inToolbar.imgLeftIcon.visibility = View.GONE
        binding.inToolbar.imgNotification.visibility = View.GONE
        binding.inToolbar.tvToolbarTitle.visibility = View.VISIBLE
        binding.inToolbar.tvToolbarTitle.text = getString(R.string.verification)

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, DriverLicenceActivity::class.java))
        }

    }

    private fun initRv() {
        //Car adapter
        val linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.rvSteps.layoutManager = linearLayoutManager
        stepsAdapter = StepsAdapter(verificationList,"info",
            object : StepsAdapter.OnVehicleClick {
                override fun onClick(position: Int) {

                }
            })
        binding.rvSteps.adapter = stepsAdapter
    }
}