package com.example.dm.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.dm.presentation.adapter.ChatAdapter
import com.example.dm.databinding.FragmentChatBinding
import com.example.dm.data.model.UserInfo
import com.example.dm.data.viewmodel.ViewModel
import com.example.dm.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class chat : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var userList: ArrayList<UserInfo>
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        varibaleInit()
        subscribeUi()

        return binding.root
    }

    private fun subscribeUi() {
        getUserList()
    }

    private fun getUserList() {

        viewModel.getUserList { userList ->
            val users = mutableListOf<UserInfo>()
            for (user in userList) {
                if (user.uid != FirebaseUtils.firebaseAuth.uid) {
                    users.add(user)
                }
            }
            binding.rc.adapter = ChatAdapter(requireContext(), users)
        }
    }

    private fun varibaleInit() {
        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        binding = FragmentChatBinding.inflate(layoutInflater)
        userList = ArrayList()
        auth = FirebaseUtils.firebaseAuth
        database = FirebaseUtils.firebaseDatabase
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

        database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("activeStatus")
            .setValue(status)
    }

}