package com.example.dm.notification

data class PushNotification(
    val to: String = "",
    val data: NotificationModel
)