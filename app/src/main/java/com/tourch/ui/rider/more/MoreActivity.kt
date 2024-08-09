package com.tourch.ui.rider.more

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.tourch.annotation.Status
import com.tourch.databinding.ActivityMoreBinding
import com.tourch.databinding.ActivitySplashBinding
import com.tourch.retrofit.EncryptedDataResponse
import com.tourch.retrofit.WebResponse
import com.tourch.ui.login.OtpVerifyActivity
import com.tourch.ui.login.viewModel.LoginViewModel
import com.tourch.ui.rider.more.history.RideHistoryActivity
import com.tourch.ui.rider.more.ref.FreebiesActivity
import com.tourch.ui.rider.more.ref.ReferralsActivity
import com.tourch.ui.rider.setting.SettingActivity
import com.tourch.utils.CommonUtils

class MoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoreBinding
    private lateinit var moreViewModel: MoreViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObserver()
        initView()
        setToolBar()
    }

    private fun setToolBar() {
        binding.toolbar.imgLeftIcon.visibility = View.VISIBLE
        binding.toolbar.tvToolbarTitle.visibility = View.VISIBLE
        binding.toolbar.imgLogo.visibility = View.GONE

        binding.toolbar.tvToolbarTitle.text = "More"

        binding.toolbar.imgLeftIcon.setOnClickListener {
            finish()
        }
    }



    private fun initView() {

        binding.llMyTrip.setOnClickListener {
            Toast.makeText(this,"Module Under development", Toast.LENGTH_SHORT).show()
        }

        binding.llRideHistory.setOnClickListener {
            startActivity(Intent(this, RideHistoryActivity::class.java))
        }

        binding.llWallet.setOnClickListener {

        }

        binding.llDList.setOnClickListener {

        }

        binding.llRef.setOnClickListener {
            startActivity(Intent(this, ReferralsActivity::class.java))
        }

        binding.llFree.setOnClickListener {
            startActivity(Intent(this, FreebiesActivity::class.java))
        }

        binding.llOurRoutes.setOnClickListener {

        }

        binding.llSetting.setOnClickListener {
            startActivity(Intent(this, SettingActivity::class.java))
        }

        binding.llHelp.setOnClickListener {

        }
    }

    private fun initObserver() {
        moreViewModel = ViewModelProvider(this)[MoreViewModel::class.java]
        moreViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                CommonUtils.showLoading(this)
            else
                CommonUtils.hideLoading(this)
        }

        moreViewModel.responseSendOtpData.observe(this) { response: WebResponse<EncryptedDataResponse> ->
            if (response.status == Status.SUCCESS) {
                Toast.makeText(this, response.errorMsg+"Hp gya h", Toast.LENGTH_SHORT).show()

                val decrypt = CommonUtils.decryptData(response.data!!.payload.data)

                /*val myType = object : TypeToken<SendOtpApiResponse>() {}.type
                val responseData = Gson().fromJson<SendOtpApiResponse>(decrypt, myType)*/

                /*val intent = Intent(this, OtpVerifyActivity::class.java)
                intent.putExtra("comeFrom", comeFrom)
                intent.putExtra("userRole", userRole)
                intent.putExtra("countryCode", binding.edCCode.text.toString().trim())
                intent.putExtra("mobileNo", binding.edPhone.text.toString().trim())
                startActivity(intent)*/
            }
            if (response.status == Status.FAILURE) {
                Toast.makeText(this, response.errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
        moreViewModel = ViewModelProvider(this)[MoreViewModel::class.java]
        moreViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                CommonUtils.showLoading(this)
            else
                CommonUtils.hideLoading(this)
        }
    }
}