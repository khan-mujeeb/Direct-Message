package com.example.dm.data.repository

import androidx.core.app.NotificationCompat
import com.example.dm.R
import com.example.dm.data.model.Message
import com.example.dm.data.model.UserInfo
import com.example.dm.notification.NotificationModel
import com.example.dm.notification.PushNotification
import com.example.dm.notification.api.ApiUtlis
import com.example.dm.utils.ConstUtils.channelId
import com.example.dm.utils.ConstUtils.message
import com.example.dm.utils.FirebaseUtils
import com.example.dm.utils.FirebaseUtils.chatRef
import com.example.dm.utils.FirebaseUtils.firebaseAuth
import com.example.dm.utils.FirebaseUtils.firebaseDatabase
import com.example.dm.utils.FirebaseUtils.userRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Response

class Repository {

    // fun to check user is created or not
    fun getUserList(callback: (List<UserInfo>) -> Unit) {
        val userList = mutableListOf<UserInfo>()
        userRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (user in snapshot.children) {
                    val temp = user.getValue(UserInfo::class.java)!!
                    userList.add(temp)
                }

                callback(userList)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }


    /*
    send message
     */
    fun sendMessages(
        senderRoom: String,
        reciverRoom: String,
        message: Message,
        randomkey: String,
        recever_fcm_token: String
    ) {
        firebaseDatabase.reference
            .child("chats")
            .child(senderRoom)
            .child("message")
            .child(randomkey)
            .setValue(message)
            .addOnSuccessListener {

                val notification = PushNotification(
                    data = NotificationModel(
                        title = message.senderName,
                        body = message.message
                    ),
                    to = recever_fcm_token
                )

                ApiUtlis.getInstance().sendNotification(notification).enqueue(object : retrofit2.Callback<PushNotification>{
                    override fun onResponse(
                        call: Call<PushNotification>,
                        response: Response<PushNotification>
                    ) {

                    }

                    override fun onFailure(call: Call<PushNotification>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })

                firebaseDatabase.reference
                    .child("chats")
                    .child(reciverRoom)
                    .child("message")
                    .child(randomkey)
                    .setValue(message)
                    .addOnSuccessListener {

                    }

            }
    }

    /*
    get fcm tokken
     */
    fun getFcmToken(receverId: String, callback: (String) -> Unit) {
        userRef.child(receverId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UserInfo::class.java)
                    callback(user!!.fcm_token)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    /*
    get user info
     */
    fun updateUserInfo(name: String, about: String, userId: String) {
        val updates = mapOf<String, Any>(
            "name" to name,
            "about" to about
        )
        userRef.child(userId).updateChildren(updates)

    }


    /*
    delete sender message
     */
    fun deleteSenderMessage(senderRoom: String, messageId: String) {
        chatRef.child(senderRoom).child(message).child(messageId).removeValue()
        println("sender $senderRoom")
    }

    /*
    delete reciver message
    */
    fun deleteReciverMessage(reciverRoom: String, messageId: String) {

        chatRef.child(reciverRoom).child(message).child(messageId).removeValue()
    }

    /*
    add contact
     */
    fun addContact(user: UserInfo) {
        val randomkey = firebaseDatabase.reference.push().key!!
        FirebaseUtils.contactRef.child(firebaseAuth.currentUser?.phoneNumber.toString())
            .child(randomkey)
            .setValue(user)
    }


}