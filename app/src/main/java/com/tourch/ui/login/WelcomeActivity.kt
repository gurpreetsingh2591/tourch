package com.tourch.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tourch.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, UserTypeActivity::class.java)
            intent.putExtra("comeFrom", "registration")
            startActivity(intent)
        }

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this, SendOtpActivity::class.java)
            intent.putExtra("comeFrom", "signin")
            startActivity(intent)
        }
    }
}