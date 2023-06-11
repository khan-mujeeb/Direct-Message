package com.example.dm.presentation.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dm.presentation.activity.MainActivity
import com.example.dm.databinding.ActivityNumberBinding
import com.example.dm.utils.FirebaseUtils
import com.example.dm.utils.FirebaseUtils.firebaseUser



class Number : AppCompatActivity() {
    private lateinit var binding: ActivityNumberBinding
    private lateinit var number: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        subscribeUi()
        goToSignup()
        subscribeCLickEvent()

    }

    private fun subscribeUi() {
        // if user is login
        if (firebaseUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    private fun subscribeCLickEvent() {
        // go to otp activity
        binding.getOtp.setOnClickListener {
            number = binding.numberEt.editText!!.text.toString()
            if (checkEditText(number)) {
                goToOTPActivity()
            } else {
                Toast.makeText(this, "enter correct number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToOTPActivity() {
        val intent = Intent(this, OTP::class.java)
        intent.putExtra("number", number)
        startActivity(intent)
        finish()
    }

    fun goToSignup() {
        if(firebaseUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    // function to check edittext is empty or not
    fun checkEditText(number: String): Boolean {

        if (number.isNotEmpty() && number.length == 10) {
            return true
        }
        return false
    }
}