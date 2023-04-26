package com.example.dm.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dm.R
import com.example.dm.databinding.ActivityMainBinding
import com.example.dm.presentation.adapter.ViewPagerAdapter
import com.example.dm.presentation.ui.chat
import com.example.dm.presentation.ui.status

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialisation of declared variable
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        subscribeUi()

    }

    private fun subscribeUi() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        switchFragment()
    }

    fun switchFragment() {
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(chat())
        fragmentList.add(status())

        val adapter = ViewPagerAdapter(this, supportFragmentManager, fragmentList)
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editProfile -> {
                startActivity(Intent(this, EditProfile::class.java))
            }
            R.id.setting -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
            R.id.logout -> {
                Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
            }
            R.id.addContact -> Toast.makeText(this, "add contact", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}

