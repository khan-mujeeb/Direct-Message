package com.example.dm.presentation.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dm.R
import com.example.dm.data.model.UserInfo
import com.example.dm.data.viewmodel.ViewModel
import com.example.dm.databinding.ActivityMainBinding
import com.example.dm.databinding.SearchAlertDialogBinding
import com.example.dm.presentation.adapter.ViewPagerAdapter
import com.example.dm.presentation.ui.chat
import com.example.dm.presentation.ui.status
import com.example.dm.utils.DialogUtils

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var dialogBinding: SearchAlertDialogBinding
    lateinit var viewModel: ViewModel
    lateinit var loadingDialog: androidx.appcompat.app.AlertDialog
    lateinit var  dialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initialisation of declared variable
        binding = ActivityMainBinding.inflate(layoutInflater)
        dialogBinding = SearchAlertDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        variableInit()
        subscribeUi()

    }

    private fun variableInit() {
        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        loadingDialog = DialogUtils.buildLoadingDialog(this@MainActivity)
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

        val adapter = ViewPagerAdapter(this@MainActivity, supportFragmentManager, fragmentList)
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    private fun showSearchDialog() {
        dialog = Dialog(this)
        val dialogBinding = SearchAlertDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(false)

        dialogBinding.cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.seachUser.setOnClickListener {
            val number = dialogBinding.name.editText!!.text.toString().let {
                if (it.isNotEmpty()) {
                    "+91$it"
                } else {
                    Toast.makeText(this, "Enter phonenumber", Toast.LENGTH_SHORT).show()
                    null
                }
            }
            searchUser(number)
        }

        dialog.show()
    }

    private fun searchUser(number: String?) {
        if (number != null) {
            loadingDialog.show()
            viewModel.getUserList { userInfos ->
                val sortedList = userInfos.sortedBy { it.phonenumber }
                val index = sortedList.binarySearch { it.phonenumber.compareTo(number) }
                loadingDialog.dismiss()
                if (index >= 0) {
                    val matchingUser = sortedList[index]
                    addContact(matchingUser)
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addContact(matchingUser: UserInfo) {
        Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show()
        dialog.dismiss()
        viewModel.addContact(matchingUser)

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
            R.id.addContact -> {
                showSearchDialog()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}

