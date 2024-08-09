package com.tourch.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tourch.R
import com.tourch.annotation.Status
import com.tourch.application.Constants
import com.tourch.databinding.ActivityOtpVerifyBinding
import com.tourch.databinding.ActivitySendOtpBinding
import com.tourch.retrofit.EncryptedDataResponse
import com.tourch.retrofit.WebResponse
import com.tourch.ui.login.viewModel.LoginViewModel
import com.tourch.ui.rider.HomeActivity
import com.tourch.utils.CommonUtils
import java.util.*
import java.util.concurrent.TimeUnit

class OtpVerifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpVerifyBinding
    private lateinit var loginViewModel: LoginViewModel
    var timer: CountDownTimer? = null
    var otpEnter = false
    private var comeFrom = ""
    private var userRole = ""
    private var countryCode = ""
    private var mobileNo = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentComes = intent
        comeFrom = intentComes.getStringExtra("comeFrom").toString()
        userRole = intentComes.getStringExtra("userRole").toString()
        countryCode = intentComes.getStringExtra("countryCode").toString()
        mobileNo = intentComes.getStringExtra("mobileNo").toString()

        setToolBar()
        setResendTimer()
        initObserver()
        initView()
    }
    private fun setToolBar() {
        binding.toolbar.imgLeftIcon.visibility = View.GONE
        binding.toolbar.tvToolbarTitle.visibility = View.GONE
        binding.toolbar.imgLogo.visibility = View.VISIBLE

    }

    private fun initView() {

        binding.pinview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6){
                    otpEnter = true
                    binding.btnNext.background.setTint(ContextCompat.getColor(this@OtpVerifyActivity, R.color.colorPrimary))
                }else{
                    otpEnter = false
                }
            }

        })

        binding.tvResend.setOnClickListener{
            callSendOtpApi(comeFrom)
        }

        binding.llBack.setOnClickListener {
            finish()
        }

        binding.btnNext.setOnClickListener{
            if (otpEnter){
                callVerifyOtpApi(comeFrom)
            }else{
                CommonUtils.showToast(this,"Please enter OTP")
            }
        }
    }

    private fun initObserver() {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                CommonUtils.showLoading(this)
            else
                CommonUtils.hideLoading(this)
        }

        loginViewModel.responseSendOtpData.observe(this) { response: WebResponse<EncryptedDataResponse> ->
            if (response.status == Status.SUCCESS) {
                Toast.makeText(this, response.errorMsg+"responseSendOtpData", Toast.LENGTH_SHORT).show()

            }
            if (response.status == Status.FAILURE) {
                Toast.makeText(this, response.errorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.responseVerifyOtpData.observe(this) { response: WebResponse<EncryptedDataResponse> ->
            if (response.status == Status.SUCCESS) {
                Toast.makeText(this, response.errorMsg+"VerifyOtpData", Toast.LENGTH_SHORT).show()

                val decrypt = CommonUtils.decryptData(response.data!!.payload.data)
                Log.e("TAG", "Respose Ongoing Api==>" + decrypt)

                if (comeFrom == "signin"){
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }else{
                    val intent = Intent(this, RegistrationActivity::class.java)
                    intent.putExtra("comeFrom", comeFrom)
                    intent.putExtra("userRole", userRole)
                    intent.putExtra("countryCode", countryCode)
                    intent.putExtra("mobileNo", mobileNo)
                    startActivity(intent)
                }

            }
            if (response.status == Status.FAILURE) {
                Toast.makeText(this, response.errorMsg, Toast.LENGTH_SHORT).show()
            }
        }


        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                CommonUtils.showLoading(this)
            else
                CommonUtils.hideLoading(this)
        }
    }


    private fun callSendOtpApi(type: String) {
        if (CommonUtils.isInternetAvailable(this)) {
            CommonUtils.showLoading(this)
            val params = HashMap<String, Any>()
            params[Constants.Param.code] = "EN"
            params[Constants.Param.country_code] = countryCode
            params[Constants.Param.mobile] = CommonUtils.encryptString(mobileNo)
            params[Constants.Param.user_type] = ""//optional
            params[Constants.Param.type] = type
            Log.e("TAG", "callSignUpApi==>" + Gson().toJson(params))

            loginViewModel.callSendOtpAPI(CommonUtils.encryptData(params))

        }
    }

    private fun callVerifyOtpApi(type: String) {
        if (CommonUtils.isInternetAvailable(this)) {
            CommonUtils.showLoading(this)
            val params = HashMap<String, Any>()
            params[Constants.Param.code] = "EN"
            params[Constants.Param.mobile] = CommonUtils.encryptString(mobileNo)
            params[Constants.Param.otp] = binding.pinview.text.toString()
            params[Constants.Param.type] = type
            Log.e("TAG", "callSignUpApi==>" + Gson().toJson(params))

            loginViewModel.callVerifyOtpAPI(CommonUtils.encryptData(params))

        }
    }



    private fun setResendTimer() {
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished > 10000) {
                    val word: Spannable = SpannableString(
                        String.format(
                            Locale.US, "%02d:%d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                        )
                    )


                    val mySpannableString =
                        SpannableString(getString(R.string.i_didn_t_receive_a_code) + "(" + word + ")")
                    mySpannableString.setSpan(UnderlineSpan(), 0, mySpannableString.length, 0)
                    binding.tvResend.text = mySpannableString
                    binding.tvResend.setTextColor(ContextCompat.getColor(this@OtpVerifyActivity, R.color.color8C8C9C))
                } else {
                    val word: Spannable = SpannableString(
                        String.format(
                            Locale.US, "%02d:0%d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(
                                            millisUntilFinished
                                        )
                                    )
                        )
                    )
                    val mySpannableString =
                        SpannableString(getString(R.string.i_didn_t_receive_a_code) + "(" + word + ")")
                    mySpannableString.setSpan(UnderlineSpan(), 0, mySpannableString.length, 0)
                    binding.tvResend.text = mySpannableString
                    binding.tvResend.setTextColor(ContextCompat.getColor(this@OtpVerifyActivity, R.color.color8C8C9C))
                    //   tvResend.text = word
                }
            }

            override fun onFinish() {
                if (timer != null) {
                    timer!!.cancel()
                }
                try {
                    val mySpannableString =
                        SpannableString(getString(R.string.i_didn_t_receive_a_code) + "(0:00)")
                    mySpannableString.setSpan(UnderlineSpan(), 0, mySpannableString.length, 0)
                    binding.tvResend.text = mySpannableString
                    binding.tvResend.setTextColor(ContextCompat.getColor(this@OtpVerifyActivity, R.color.color0084FE))

                    // tvResend.text = "RESEND"
                } catch (e: java.lang.Exception) {
                }

            }
        }.start()
    }

}