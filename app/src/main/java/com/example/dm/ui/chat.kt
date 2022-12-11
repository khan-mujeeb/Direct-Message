package com.example.dm.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentContainer
import com.example.dm.MainActivity
import com.example.dm.R
import com.example.dm.adapter.ChatAdapter
import com.example.dm.databinding.FragmentChatBinding
import com.example.dm.model.UserInfo
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)


        userList = ArrayList()
        auth = FirebaseUtils.firebaseAuth
        database = FirebaseUtils.firebaseDatabase



//        fetching users from firebase database
        database.reference.child("users")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()

                    for (snapshot1 in snapshot.children) {
                        val user = snapshot1.getValue(UserInfo::class.java)
                        if(user!!.uid!=FirebaseUtils.firebaseAuth.uid) {
                            userList.add(user)
                        }
                    }
                    binding.rc.adapter = ChatAdapter(requireContext(),userList)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        return binding.root
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