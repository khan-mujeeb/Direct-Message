package com.example.dm.notification.api

import com.example.dm.notification.PushNotification
import com.example.dm.utils.ConstUtils.firebase_key
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
     @Headers(
         "Content-Type:application/json",
         "Authorization:key=$firebase_key"
     )

     @POST("fcm/send")

     fun sendNotification(@Body notification: PushNotification): Call<PushNotification>
}