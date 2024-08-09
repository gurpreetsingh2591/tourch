package com.tourch.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tourch.annotation.Status
import com.tourch.application.Constants
import com.tourch.databinding.ActivityTncBinding
import com.tourch.retrofit.EncryptedDataResponse
import com.tourch.retrofit.WebResponse
import com.tourch.ui.login.viewModel.LoginViewModel
import com.tourch.utils.CommonUtils
import com.tourch.utils.CommonUtils.encryptString

class TncActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTncBinding
    private lateinit var loginViewModel: LoginViewModel

    private var comeFrom = ""
    private var userRole = ""
    private var countryCode = ""
    private var mobileNo = ""
    private var fName = ""
    private var lName = ""
    private var email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTncBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentComes = intent
        comeFrom = intentComes.getStringExtra("comeFrom").toString()
        userRole = intentComes.getStringExtra("userRole").toString()
        countryCode = intentComes.getStringExtra("countryCode").toString()
        mobileNo = intentComes.getStringExtra("mobileNo").toString()
        fName = intentComes.getStringExtra("fName").toString()
        lName = intentComes.getStringExtra("lName").toString()
        email = intentComes.getStringExtra("email").toString()


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
        binding.checkBox.isChecked

        binding.llBack.setOnClickListener {
            finish()
        }

        binding.btnNext.setOnClickListener {
            if (binding.checkBox.isChecked) {
                callSingUpApi(userRole)
            } else {
                CommonUtils.showToast(this, "Please accept T&C")
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

        loginViewModel.responseSignUpData.observe(this) { response: WebResponse<EncryptedDataResponse> ->
            if (response.status == Status.SUCCESS) {
                Toast.makeText(this,  "SignUp Data", Toast.LENGTH_SHORT).show()

                val decrypt = CommonUtils.decryptData(response.data!!.payload.data)
/*                val myType = object : TypeToken<SendOtpApiResponse>() {}.type
                val responseData = Gson().fromJson<SendOtpApiResponse>(decrypt, myType)*/
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

    private fun callSingUpApi(type: String) {
        if (CommonUtils.isInternetAvailable(this)) {
            CommonUtils.showLoading(this)
            val params = HashMap<String, Any>()

            params[Constants.Param.code] = "EN"
            params[Constants.Param.country_code] = countryCode
            params[Constants.Param.mobile] = encryptString(mobileNo)
            params[Constants.Param.first_name] = encryptString(fName)
            params[Constants.Param.last_name] = encryptString(lName)
            params[Constants.Param.email] = encryptString(email)
            params[Constants.Param.register_type] = "normal"
            params[Constants.Param.current_profile] = type
            Log.e("TAG", "callSignUpApi==>" + Gson().toJson(params))

            loginViewModel.callSignUpAPI(CommonUtils.encryptData(params))
        }
    }

}