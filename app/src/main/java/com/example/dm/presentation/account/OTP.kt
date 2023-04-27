package com.example.dm.presentation.account

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dm.presentation.activity.MainActivity
import com.example.dm.databinding.ActivityOtpBinding
import com.example.dm.data.model.UserInfo
import com.example.dm.data.viewmodel.ViewModel
import com.example.dm.utils.DialogUtils.buildLoadingDialog
import com.example.dm.utils.FirebaseUtils
import com.example.dm.utils.FirebaseUtils.firebaseAuth
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class OTP : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var dialog: AlertDialog
    private lateinit var verificationId: String
    private lateinit var phonenumber: String
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOtpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        variableInit()
        subscribeUi()

        val options = getOTP()

        PhoneAuthProvider.verifyPhoneNumber(options)
        subscribeOnClickEvents()

    }

    private fun subscribeOnClickEvents() {
        binding.verify.setOnClickListener {

            val otp = binding.otpEt.editText!!.text.toString()
            if (otp.isNotEmpty()) {
                dialog.show()
                otpVerification(otp)
            } else {
                Toast.makeText(this@OTP, "Enter OTP", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getOTP(): PhoneAuthOptions {
        dialog.show()
        return PhoneAuthOptions.newBuilder(FirebaseUtils.firebaseAuth)
            .setPhoneNumber(phonenumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this@OTP)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                    Toast.makeText(this@OTP, "Verification Failed $p0", Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    dialog.dismiss()
                    verificationId = p0
                    dialog.dismiss()
                    println("mujeeb $verificationId")
                }

            }).build()
    }

    private fun otpVerification(otp: String) {

        // otp verification
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        FirebaseUtils.firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                dialog.dismiss()
                checkUserAviablity()
                startActivity(Intent(this, Profile::class.java))
                finish()
            } else {
                dialog.dismiss()
                Toast.makeText(this@OTP, "${it.exception}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun subscribeUi() {
        binding.numberTv.text = phonenumber
    }

    private fun variableInit() {

        phonenumber = "+91" + intent.getStringExtra("number").toString()

        viewModel = ViewModelProvider(this@OTP)[ViewModel::class.java]
        // loading dialog
        dialog = buildLoadingDialog(this@OTP)
    }


    private fun checkUserAviablity() {
        dialog.show()
        viewModel.getUserList {userList ->
            for (user in userList) {
                if (user.phonenumber == firebaseAuth.currentUser!!.phoneNumber) {
                    dialog.dismiss()
                    Toast.makeText(this@OTP, "welcome", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@OTP, MainActivity::class.java))
                }
            }
            dialog.dismiss()
        }
    }

}