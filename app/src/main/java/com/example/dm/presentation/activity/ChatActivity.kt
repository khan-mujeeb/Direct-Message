package com.example.dm.presentation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dm.presentation.adapter.MessageAdapter
import com.example.dm.data.model.Message
import com.example.dm.data.viewmodel.ViewModel
import com.example.dm.databinding.ActivityChatBinding
import com.example.dm.utils.FirebaseUtils
import com.example.dm.utils.FirebaseUtils.chatRef
import com.example.dm.utils.FirebaseUtils.userRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    // firebase variable's
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    // uid's
    private lateinit var senderUid: String
    private lateinit var reciverUid: String
    private lateinit var senderRoom: String
    private lateinit var reciverRoom: String

    // user data variable
    private lateinit var list: ArrayList<Message>
    private lateinit var img: String
    private lateinit var name: String
    lateinit var viewModel: ViewModel
    lateinit var recever_fcm_token: String


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        subscribeOnClickEvents()
        variableInit()
        subscribeUi()

    }

    private fun subscribeUi() {
        // setting username and dp
        binding.personName.text = name
        Glide.with(this).load(img).into(binding.personImg)


        // fetching most recent message from firebase realtime database
        chatRef.child(senderRoom)
            .child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (snapshot1 in snapshot.children) {
                        val data = snapshot1.getValue(Message::class.java)
                        list.add(data!!)
                    }

                    var activeStatus = intent.getStringExtra("active")
                    if (activeStatus == "online") {
                        binding.onlineStatus.visibility = View.VISIBLE
                        binding.offilneStatus.visibility = View.GONE
                    } else {
                        binding.onlineStatus.visibility = View.GONE
                        binding.offilneStatus.visibility = View.VISIBLE
                    }
                    binding.messageRc.adapter = MessageAdapter(
                        this@ChatActivity,
                        list,
                        senderRoom,
                        reciverRoom,
                        viewModel
                    )
                    binding.messageRc.scrollToPosition(list.size - 1)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun subscribeOnClickEvents() {
        // updating new text to firebase realtime database
        binding.sendBtn.setOnClickListener {

            val senderName = "auth.currentUser!!.phoneNumber.toString()"
            val randomkey = database.reference.push().key!!

            val textMeassage = binding.message.text

            // creating message
            val message = Message(
                message = textMeassage.toString(),
                sendUid =  senderUid,
                timeSTamp = Date().time,
                messageId = randomkey,
                senderName = senderName
            )


//            sending message
            if (textMeassage.isNotEmpty()) {
                binding.message.text = null
                viewModel.sendMessages(
                    senderRoom = senderRoom,
                    reciverRoom = reciverRoom,
                    randomkey = randomkey,
                    message = message,
                    recever_fcm_token = recever_fcm_token
                    )
            } else {
                Toast.makeText(this, "Enter your text", Toast.LENGTH_SHORT).show()
            }
        }

        binding.back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun variableInit() {
        auth = FirebaseUtils.firebaseAuth
        database = FirebaseUtils.firebaseDatabase

        viewModel = ViewModelProvider(this)[ViewModel::class.java]
        senderUid = auth.uid.toString()
        reciverUid = intent.getStringExtra("uid")!!
        senderRoom = senderUid + reciverUid
        reciverRoom = reciverUid + senderUid

        img = intent.getStringExtra("img")!!
        name = intent.getStringExtra("name")!!
        list = ArrayList()

         viewModel.getFcmToken(reciverUid) { token->
            recever_fcm_token = token
        }
    }

    override fun onResume() {
        super.onResume()
        activeStatus("online")
    }

    override fun onPause() {
        super.onPause()
        activeStatus("offline")
    }

    // updating active status of user
    fun activeStatus(status: String) {
        userRef.child(auth.uid.toString())
            .child("activeStatus")
            .setValue(status)
    }

}
