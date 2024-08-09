package com.tourch

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.tourch.databinding.ActivitySplashBinding
import com.tourch.ui.driver.verification.activity.HomeVerificationActivity
import com.tourch.utils.SharedPreferencesUtils

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var isLogin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        Handler(Looper.getMainLooper()).postDelayed({
            afterSplash()
        }, 3500)
    }

    private fun afterSplash() {
        try {
            isLogin = SharedPreferencesUtils.isUserLogin()!!
        } catch (e: NullPointerException) {

        }

        if (isLogin) {
            //startActivity(Intent(this, HomeActivity::class.java))
        } else {
            //startActivity(Intent(this, HomeActivity::class.java))
        }
        startActivity(Intent(this, HomeVerificationActivity::class.java))
        finish()
    }
}