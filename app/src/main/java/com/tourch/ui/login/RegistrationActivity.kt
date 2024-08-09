package com.tourch.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.tourch.R
import com.tourch.databinding.ActivityEmailBinding
import com.tourch.databinding.ActivityRegistrationBinding
import com.tourch.utils.CommonUtils

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    private var comeFrom = ""
    private var userRole = ""
    private var countryCode = ""
    private var mobileNo = ""

    var fName = false
    var lName = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentComes = intent
        comeFrom = intentComes.getStringExtra("comeFrom").toString()
        userRole = intentComes.getStringExtra("userRole").toString()
        countryCode = intentComes.getStringExtra("countryCode").toString()
        mobileNo = intentComes.getStringExtra("mobileNo").toString()

        setToolBar()
        initView()

    }

    private fun setToolBar() {
        binding.toolbar.imgLeftIcon.visibility = View.GONE
        binding.toolbar.tvToolbarTitle.visibility = View.GONE
        binding.toolbar.imgLogo.visibility = View.VISIBLE
    }

    private fun initView() {
        binding.edFirstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                fName = if (s?.length!! > 2){
                    if (lName)
                        binding.btnNext.background.setTint(ContextCompat.getColor(this@RegistrationActivity, R.color.colorPrimary))
                    true
                }else{
                    false
                }
            }
        })

        binding.edLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                lName = if (s?.length!! > 2 && fName){
                    binding.btnNext.background.setTint(ContextCompat.getColor(this@RegistrationActivity, R.color.colorPrimary))
                    true
                }else{
                    false
                }
            }
        })

        binding.btnNext.setOnClickListener {
            if (fName&&lName){
                val intent = Intent(this, EmailActivity::class.java)
                intent.putExtra("comeFrom", comeFrom)
                intent.putExtra("userRole", userRole)
                intent.putExtra("countryCode", countryCode)
                intent.putExtra("mobileNo", mobileNo)
                intent.putExtra("fName", binding.edFirstName.text.toString())
                intent.putExtra("lName", binding.edLastName.text.toString())
                startActivity(intent)
            }else{
                CommonUtils.showToast(this,"Please enter your First&Last Name")
            }
        }
    }
}