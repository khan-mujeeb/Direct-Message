package com.example.dm.utils

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


}