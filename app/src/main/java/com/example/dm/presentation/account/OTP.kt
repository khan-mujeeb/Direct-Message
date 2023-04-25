package com.example.dm.presentation.account

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dm.MainActivity
import com.example.dm.databinding.ActivityOtpBinding
import com.example.dm.presentation.data.model.UserInfo
import com.example.dm.utils.DialogUtils.buildLoadingDialog
import com.example.dm.utils.FirebaseUtils
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

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOtpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        variableInit()
        subscribeUi()

        dialog.show()

        // verification
        val options = PhoneAuthOptions.newBuilder(FirebaseUtils.firebaseAuth)
            .setPhoneNumber(phonenumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
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

        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.verify.setOnClickListener {

            val otp = binding.otpEt.editText!!.text.toString()
            if (otp.isNotEmpty()) {
                dialog.show()

                // phone number verification
                val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                FirebaseUtils.firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        dialog.dismiss()


                        // if user is present move directly to chat page
                        val database = FirebaseUtils.firebaseDatabase
                        database.reference.child("users")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (snapshot1 in snapshot.children) {
                                        val user = snapshot1.getValue(UserInfo::class.java)
                                        println("mobile ${user!!.phonenumber} $phonenumber")
                                        if (user!!.phonenumber == phonenumber) {
                                            startActivity(
                                                Intent(
                                                    this@OTP,
                                                    MainActivity::class.java
                                                )
                                            )
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })

                        startActivity(Intent(this, Profile::class.java))
                        finish()
                    } else {
                        dialog.dismiss()
                        Toast.makeText(this@OTP, "${it.exception}", Toast.LENGTH_LONG).show()
                    }
                }

            } else {
                Toast.makeText(this@OTP, "Enter OTP", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun subscribeUi() {
        binding.numberTv.text = phonenumber
    }

    private fun variableInit() {

        phonenumber = "+91" + intent.getStringExtra("number").toString()

        // loading dialog
        dialog = buildLoadingDialog(this@OTP)
    }


}