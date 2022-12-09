package com.example.dm.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dm.R
import com.example.dm.adapter.ChatAdapter
import com.example.dm.databinding.FragmentChatBinding
import com.example.dm.model.UserInfo
import com.example.dm.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class chat : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var userList: ArrayList<UserInfo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)

        database = FirebaseUtils.firebaseDatabase
        userList = ArrayList()

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
}