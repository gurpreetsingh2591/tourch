package com.tourch.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tourch.databinding.ActivitySliderBinding

class SliderActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySliderBinding
    private var comeFrom = ""
    private var userRole = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySliderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentComes = intent
        comeFrom = intentComes.getStringExtra("comeFrom").toString()
        userRole = intentComes.getStringExtra("userRole").toString()

        initView()
    }

    private fun initView() {

        binding.btnNext.setOnClickListener {
            binding.one.visibility = View.GONE
            binding.two.visibility = View.VISIBLE
        }

        binding.btnGetStarted.setOnClickListener {
            val intent = Intent(this, SendOtpActivity::class.java)
            intent.putExtra("comeFrom", comeFrom)
            intent.putExtra("userRole", userRole)
            startActivity(intent)
        }

    }
}