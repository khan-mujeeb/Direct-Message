package com.example.dm.utils

import com.example.dm.utils.ConstUtils.chats
import com.example.dm.utils.ConstUtils.contact
import com.example.dm.utils.ConstUtils.profile
import com.example.dm.utils.ConstUtils.userNode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

object FirebaseUtils {

        // basic ref
        var firebaseAuth = FirebaseAuth.getInstance()
        var firebaseUser = firebaseAuth.currentUser
        var firebaseStorage = FirebaseStorage.getInstance()
        var firebaseDatabase = FirebaseDatabase.getInstance()


        var userRef = firebaseDatabase.getReference(userNode)
        val chatRef = firebaseDatabase.getReference(chats)
        val contactRef = firebaseDatabase.getReference(contact)
        val storageRef = firebaseStorage.reference.child(profile)


}