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

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

}