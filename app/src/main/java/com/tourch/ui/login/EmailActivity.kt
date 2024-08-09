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

class EmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmailBinding

    private var comeFrom = ""
    private var userRole = ""
    private var countryCode = ""
    private var mobileNo = ""
    private var fName = ""
    private var lName = ""

    var isMail = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentComes = intent
        comeFrom = intentComes.getStringExtra("comeFrom").toString()
        userRole = intentComes.getStringExtra("userRole").toString()
        countryCode = intentComes.getStringExtra("countryCode").toString()
        mobileNo = intentComes.getStringExtra("mobileNo").toString()
        fName = intentComes.getStringExtra("fName").toString()
        lName = intentComes.getStringExtra("lName").toString()

        setToolBar()
        initView()
    }

    private fun setToolBar() {
        binding.toolbar.imgLeftIcon.visibility = View.GONE
        binding.toolbar.tvToolbarTitle.visibility = View.GONE
        binding.toolbar.imgLogo.visibility = View.VISIBLE
    }

    private fun initView() {

        binding.edEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                isMail = if (s?.length!! > 3){
                    if (isMail)
                        binding.btnNext.background.setTint(ContextCompat.getColor(this@EmailActivity, R.color.colorPrimary))
                    true
                }else{
                    false
                }
            }
        })

        binding.llBack.setOnClickListener {
            finish()
        }
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, TncActivity::class.java)
            intent.putExtra("comeFrom", comeFrom)
            intent.putExtra("userRole", userRole)
            intent.putExtra("countryCode", countryCode)
            intent.putExtra("mobileNo", mobileNo)
            intent.putExtra("fName", fName)
            intent.putExtra("lName", lName)
            intent.putExtra("email", binding.edEmail.text.toString())
            startActivity(intent)

        }

    }
}