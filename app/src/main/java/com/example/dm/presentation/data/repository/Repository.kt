package com.example.dm.presentation.data.repository

import androidx.activity.OnBackPressedCallback
import com.example.dm.presentation.data.model.UserInfo
import com.example.dm.utils.FirebaseUtils.userRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class Repository {

    // fun to check user is created or not
    fun getUserList(callback: ( List<UserInfo>) -> Unit) {
        val userList = mutableListOf<UserInfo>()
        userRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                for (user in snapshot.children)  {
                    val temp = user.getValue(UserInfo::class.java)!!
                    userList.add(temp)
                }

                callback(userList)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}