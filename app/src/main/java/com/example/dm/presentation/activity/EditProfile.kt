package com.example.dm.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dm.R
import com.example.dm.data.model.UserInfo
import com.example.dm.data.viewmodel.ViewModel
import com.example.dm.databinding.ActivityEditProfileBinding
import com.example.dm.utils.DialogUtils.buildLoadingDialog
import com.example.dm.utils.FirebaseUtils
import com.example.dm.utils.FirebaseUtils.firebaseUser

class EditProfile : AppCompatActivity() {
    lateinit var binding: ActivityEditProfileBinding
    lateinit var viewModel: ViewModel
    lateinit var dialog: AlertDialog
    lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeui()
        variableInit()
        getUserData()
        subscribeClickListner()

    }

    private fun subscribeClickListner() {

        binding.update.setOnClickListener{
            val name = binding.name.editText!!.text.toString()
            val about = binding.about.editText!!.text.toString()
            dialog.show()
            viewModel.updateUserInfo(name= name, about = about, userId = uid)
            dialog.dismiss()
        }
    }

    private fun getUserData() {
        viewModel.getUserList { users ->
            for (user in users) {
                if (user.uid == uid) {
                    setUserData(user)
                }
            }
        }
    }

    private fun setUserData(user: UserInfo) {
        dialog.show()
        binding.name.editText!!.setText(user.name)
        binding.phone.editText!!.setText(user.phonenumber)
        binding.about.editText!!.setText(user.about)

        dialog.dismiss()
        Glide.with(this)
            .load(user.imgUri)
            .placeholder(R.drawable.spinner)
            .into(binding.dp)


    }

    private fun variableInit() {
        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        dialog =  buildLoadingDialog(this)
        uid = firebaseUser!!.uid
    }

    private fun subscribeui() {
        setupToolbar()

    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}