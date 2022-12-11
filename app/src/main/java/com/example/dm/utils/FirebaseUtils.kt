package com.example.dm.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

object FirebaseUtils {
        var firebaseAuth = FirebaseAuth.getInstance()
        var firebaseUser = firebaseAuth.currentUser
        var firebaseStorage = FirebaseStorage.getInstance()
        var firebaseDatabase = FirebaseDatabase.getInstance()
}