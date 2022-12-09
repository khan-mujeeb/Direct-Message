package com.example.dm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.example.dm.MainActivity
import com.example.dm.R
import com.example.dm.adapter.ChatAdapter
import com.example.dm.adapter.MessageAdapter
import com.example.dm.model.Message
import com.example.dm.databinding.ActivityChatBinding
import com.example.dm.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.Date

class ChatActivity : AppCompatActivity() {
   private lateinit var binding: ActivityChatBinding
   private lateinit var senderUid: String
   private lateinit var reciverUid: String
    private lateinit var senderRoom: String
    private lateinit var reciverRoom: String
    private lateinit var list: ArrayList<Message>
    private lateinit var img:String
    private lateinit var name:String


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        senderUid = FirebaseUtils.firebaseAuth.uid.toString()
        reciverUid = intent.getStringExtra("uid")!!
        img = intent.getStringExtra("img")!!
        name = intent.getStringExtra("name")!!
        var database = FirebaseUtils.firebaseDatabase
        senderRoom = senderUid+reciverUid
        reciverRoom = reciverUid+senderUid
        list = ArrayList()

        binding.personName.text = name
        Glide.with(this).load(img).into(binding.personImg)


        database.reference.child("chats")
            .child(senderRoom)
            .child("message")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (snapshot1 in snapshot.children) {
                        val data = snapshot1.getValue(Message::class.java)
                        list.add(data!!)
                    }

                    binding.messageRc.adapter = MessageAdapter(this@ChatActivity,list)
                    binding.messageRc.smoothScrollToPosition(MessageAdapter(this@ChatActivity,list).itemCount);
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        binding.sendBtn.setOnClickListener {

            val textMeassage = binding.message.text
            val message = Message(
                textMeassage.toString(),
                senderUid,
                Date().time
            )
            val randomkey = database.reference.push().key
            if(textMeassage.isNotEmpty()) {
                database.reference
                    .child("chats")
                    .child(senderRoom)
                    .child("message")
                    .child(randomkey!!)
                    .setValue(message)
                    .addOnSuccessListener {

                        database.reference
                            .child("chats")
                            .child(reciverRoom)
                            .child("message")
                            .child(randomkey!!)
                            .setValue(message)
                            .addOnSuccessListener {
                                binding.message.text = null
                            }
                    }
            } else {
                Toast.makeText(this,"Enter your text",Toast.LENGTH_SHORT).show()
            }
        }
    }
}