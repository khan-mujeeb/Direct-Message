package com.example.dm.presentation.data.viewmodel

import com.example.dm.presentation.data.model.UserInfo
import com.example.dm.presentation.data.repository.Repository

class ViewModel(private val repository: Repository = Repository()): androidx.lifecycle.ViewModel() {

    /*
    function to get User List
     */
    fun getUserList(callback: (List<UserInfo>) -> Unit) {
        repository.getUserList { task ->
            callback(task)
        }
    }
}