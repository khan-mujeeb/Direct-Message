package com.example.dm.data.viewmodel

import com.example.dm.data.model.Message
import com.example.dm.data.model.UserInfo
import com.example.dm.data.repository.Repository
import com.example.dm.utils.FirebaseUtils

class ViewModel(private val repository: Repository = Repository()): androidx.lifecycle.ViewModel() {

    /*
    function to get User List
     */
    fun getUserList(callback: (List<UserInfo>) -> Unit) {
        repository.getUserList { task ->
            callback(task)
        }
    }

    fun sendMessages(senderRoom: String, reciverRoom: String, message: Message, randomkey: String, recever_fcm_token: String) {
        repository.sendMessages(senderRoom, reciverRoom, message, randomkey,recever_fcm_token)
    }


    fun updateUserInfo(name: String, about: String, userId: String) {
        repository.updateUserInfo(name, about, userId)
    }

    /*
    delete sender message
    */
    fun deleteSenderMessage(senderRoom: String, messageId: String) {
        repository.deleteSenderMessage(senderRoom, messageId)
    }

    /*
    delete reciver message
    */
    fun deleteReciverMessage(reciverRoom: String, messageId: String) {
        repository.deleteReciverMessage(reciverRoom, messageId)
    }

    /*
    get fcm token
     */
    fun getFcmToken(receverId: String, callback: (String) -> Unit) {
        repository.getFcmToken(receverId) { task ->
            callback(task)
        }
    }
}