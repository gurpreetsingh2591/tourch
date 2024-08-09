package com.tourch.ui.rider.more.ref

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tourch.databinding.ActivityReferralsBinding

class ReferralsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReferralsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReferralsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}