package com.example.dm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.dm.activity.Number
import com.example.dm.adapter.ViewPagerAdapter
import com.example.dm.databinding.ActivityMainBinding
import com.example.dm.ui.chat
import com.example.dm.ui.status
import com.example.dm.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        database = FirebaseUtils.firebaseDatabase
        auth = FirebaseUtils.firebaseAuth

        setContentView(binding!!.root)

        switchFragment()
        goToSignup()
    }
    fun switchFragment() {
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(chat())
        fragmentList.add(status())



        val adapter = ViewPagerAdapter(this, supportFragmentManager, fragmentList)
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    fun goToSignup() {
        if(FirebaseUtils.firebaseUser==null) {
            startActivity(Intent(this,Number::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        activeStatus("online")
    }

    override fun onPause() {
        super.onPause()
        activeStatus("offline")
    }

    fun activeStatus(status: String) {
        database.reference.child("activeStatus").child(auth.uid.toString()).setValue(status)
    }

}