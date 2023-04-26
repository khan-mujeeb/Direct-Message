package com.example.dm.data.viewmodel

import com.example.dm.data.model.Message
import com.example.dm.data.model.UserInfo
import com.example.dm.data.repository.Repository

class ViewModel(private val repository: Repository = Repository()): androidx.lifecycle.ViewModel() {

    /*
    function to get User List
     */
    fun getUserList(callback: (List<UserInfo>) -> Unit) {
        repository.getUserList { task ->
            callback(task)
        }
    }

    fun sendMessages(senderRoom: String, reciverRoom: String, message: Message, randomkey: String) {
        repository.sendMessages(senderRoom, reciverRoom, message, randomkey)
    }


    fun updateUserInfo(name: String, about: String, userId: String) {
        repository.updateUserInfo(name, about, userId)
    }
}