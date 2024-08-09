package com.tourch.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tourch.R
import com.tourch.databinding.ActivityUserTypeBinding
import com.tourch.utils.CommonUtils

class UserTypeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserTypeBinding

    private var comeFrom = ""
    private var userRole = ""
    private var roleSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentComes = intent
        comeFrom = intentComes.getStringExtra("comeFrom").toString()

        initView()
        setToolBar()
    }

    private fun setToolBar() {
        binding.toolbar.imgLeftIcon.visibility = View.GONE
        binding.toolbar.tvToolbarTitle.visibility = View.GONE
        binding.toolbar.imgLogo.visibility = View.VISIBLE
    }

    private fun initView() {
        binding.llDriver.setOnClickListener {
            userRole = "driver"
            selectedType(binding.llDriver, binding.radioDriver, binding.textDriver)
            unSelectedType(binding.llPassenger, binding.radioPassenger, binding.textPassenger)
        }

        binding.llPassenger.setOnClickListener {
            userRole = "passenger"
            unSelectedType(binding.llDriver, binding.radioDriver, binding.textDriver)
            selectedType(binding.llPassenger, binding.radioPassenger, binding.textPassenger)
        }

        binding.btnContinue.setOnClickListener {
            if (roleSelected) {
                if (comeFrom == "signin") {
                    val intent = Intent(this, SendOtpActivity::class.java)
                    intent.putExtra("comeFrom", comeFrom)
                    intent.putExtra("userRole", userRole)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, SliderActivity::class.java)
                    intent.putExtra("comeFrom", comeFrom)
                    intent.putExtra("userRole", userRole)
                    startActivity(intent)
                }
            } else {
                CommonUtils.showToast(this, "Please select user role.")
            }
        }
    }

    private fun selectedType(layout: RelativeLayout, radio: ImageView, text: TextView) {
        radio.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_selected_white))
        layout.background = ContextCompat.getDrawable(this, R.drawable.bg_edit_green)
        text.setTextColor(ContextCompat.getColor(this, R.color.white))

        roleSelected = true
        binding.btnContinue.background.setTint(ContextCompat.getColor(this, R.color.colorPrimary))
    }

    private fun unSelectedType(layout: RelativeLayout, radio: ImageView, text: TextView) {
        radio.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_radio_off))
        layout.background = ContextCompat.getDrawable(this, R.drawable.bg_edit_grey)
        text.setTextColor(ContextCompat.getColor(this, R.color.black))
    }
}