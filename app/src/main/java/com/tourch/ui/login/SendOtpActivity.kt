package com.tourch.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tourch.R
import com.tourch.annotation.Status
import com.tourch.application.Constants
import com.tourch.databinding.ActivitySendOtpBinding
import com.tourch.retrofit.EncryptedDataResponse
import com.tourch.retrofit.WebResponse
import com.tourch.ui.login.viewModel.LoginViewModel
import com.tourch.utils.CommonUtils.decryptData
import com.tourch.utils.CommonUtils.encryptData
import com.tourch.utils.CommonUtils.encryptString
import com.tourch.utils.CommonUtils.hideLoading
import com.tourch.utils.CommonUtils.isInternetAvailable
import com.tourch.utils.CommonUtils.showLoading
import com.tourch.utils.CommonUtils.showToast

class SendOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendOtpBinding
    private lateinit var loginViewModel: LoginViewModel

    private var comeFrom = ""
    private var userRole = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentComes = intent
        comeFrom = intentComes.getStringExtra("comeFrom").toString()
        userRole = intentComes.getStringExtra("userRole").toString()

        initObserver()
        initView()
        setToolBar()
    }

    private fun setToolBar() {
        binding.toolbar.imgLeftIcon.visibility = View.GONE
        binding.toolbar.tvToolbarTitle.visibility = View.GONE
        binding.toolbar.imgLogo.visibility = View.VISIBLE
    }

    private fun initView() {
        binding.edPhone.requestFocus()
        binding.edPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.isNotEmpty()) {
                    binding.btnNext.isClickable = true
                    binding.btnNext.background.setTint(
                        ContextCompat.getColor(
                            this@SendOtpActivity,
                            R.color.colorPrimary
                        )
                    )
                } else {
                    binding.btnNext.isClickable = false
                    binding.btnNext.background.setTint(
                        ContextCompat.getColor(
                            this@SendOtpActivity,
                            R.color.color979797
                        )
                    )
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.edCCode.text = binding.ccp.selectedCountryCodeWithPlus
        binding.ccp.setOnCountryChangeListener {
            binding.edCCode.text = binding.ccp.selectedCountryCodeWithPlus
        }

        binding.btnNext.setOnClickListener {
            if (binding.edPhone.text.toString().isNotEmpty()) {
                callSendOtpApi(comeFrom)
            } else {
                showToast(this, "Please Enter phone number")
            }
        }

        binding.llBack.setOnClickListener{
            finish()
        }
    }
    private fun initObserver() {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                showLoading(this)
            else
                hideLoading(this)
        }

        loginViewModel.responseSendOtpData.observe(this) { response: WebResponse<EncryptedDataResponse> ->
            if (response.status == Status.SUCCESS) {
                Toast.makeText(this, response.errorMsg+"SendOtpData", Toast.LENGTH_SHORT).show()

                val decrypt = decryptData(response.data!!.payload.data)
                /*val myType = object : TypeToken<SendOtpApiResponse>() {}.type
                val responseData = Gson().fromJson<SendOtpApiResponse>(decrypt, myType)*/

                val intent = Intent(this, OtpVerifyActivity::class.java)
                intent.putExtra("comeFrom", comeFrom)
                intent.putExtra("userRole", userRole)
                intent.putExtra("countryCode", binding.edCCode.text.toString().trim())
                intent.putExtra("mobileNo", binding.edPhone.text.toString().trim())
                startActivity(intent)
            }
            if (response.status == Status.FAILURE) {
                Toast.makeText(this, response.errorMsg, Toast.LENGTH_SHORT).show()
            }
        }
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                showLoading(this)
            else
                hideLoading(this)
        }
    }
    @SuppressLint("CheckResult")
    private fun callSendOtpApi(type: String) {
        if (isInternetAvailable(this)) {
            showLoading(this)
            val params = HashMap<String, Any>()
            params[Constants.Param.code] = "EN"
            params[Constants.Param.country_code] = binding.edCCode.text.toString().trim()
            params[Constants.Param.mobile] = encryptString(binding.edPhone.text.toString().trim())
            params[Constants.Param.user_type] = ""//optional
            params[Constants.Param.type] = type
            Log.e("TAG", "callSignUpApi==>" + Gson().toJson(params))

            loginViewModel.callSendOtpAPI(encryptData(params))

        }
    }
}