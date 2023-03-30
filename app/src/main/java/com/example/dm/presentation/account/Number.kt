package com.example.dm.presentation.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dm.MainActivity
import com.example.dm.databinding.ActivityNumberBinding
import com.example.dm.utils.FirebaseUtils

class Number : AppCompatActivity() {
    private lateinit var binding: ActivityNumberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // if user is login
        if(FirebaseUtils.firebaseUser!=null) {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        // go to otp activity
        binding.getOtp.setOnClickListener{

            if(checkEditText()) {
                val intent = Intent(this, OTP::class.java)
//                Toast.makeText(this,"Verification Failed ${getNumber()}",Toast.LENGTH_SHORT).show()
                intent.putExtra("number",getNumber())
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this,"enter correct number",Toast.LENGTH_SHORT).show()
            }
        }

    }

    // function to check edittext is empty or not
    fun checkEditText():Boolean {

            if (binding.one.text.toString().isNotEmpty() && binding.one.text.toString().length==10) {
                return true
            }
        return false
    }

    // getter to get number
    fun getNumber():String {
        return binding.one.text.toString()
    }
}