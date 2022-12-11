package com.example.dm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dm.MainActivity
import com.example.dm.R
import com.example.dm.adapter.ChatAdapter
import com.example.dm.adapter.MessageAdapter
import com.example.dm.model.Message
import com.example.dm.databinding.ActivityChatBinding
import com.example.dm.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

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
    private lateinit var img:String
    private lateinit var name:String


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

        // initialisation of declared variable
        auth = FirebaseUtils.firebaseAuth
        database = FirebaseUtils.firebaseDatabase

        senderUid = auth.uid.toString()
        reciverUid = intent.getStringExtra("uid")!!
        senderRoom = senderUid+reciverUid
        reciverRoom = reciverUid+senderUid

        img = intent.getStringExtra("img")!!
        name = intent.getStringExtra("name")!!
        list = ArrayList()

        // setting username and dp
        binding.personName.text = name
        Glide.with(this).load(img).into(binding.personImg)


        // fetching most recent message from firebase realtime database
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

                    var activeStatus = intent.getStringExtra("active")
                    if(activeStatus=="online") {
                        binding.onlineStatus.visibility = View.VISIBLE
                        binding.offilneStatus.visibility = View.GONE
                    }else {
                        binding.onlineStatus.visibility = View.GONE
                        binding.offilneStatus.visibility = View.VISIBLE
                    }
                    binding.messageRc.adapter = MessageAdapter(this@ChatActivity,list)
                    binding.messageRc.smoothScrollToPosition(MessageAdapter(this@ChatActivity,list).itemCount);
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        // updating new text to firebase realtime database
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
        database.reference
            .child("users")
            .child(auth.uid.toString())
            .child("activeStatus")
            .setValue(status)
    }

}



//                    // left swipe to delete the data from firebase realtime db and recycler view
//                    var itemTouchHelperCallbacks = object : ItemTouchHelper.SimpleCallback(0,
//                        ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
//                    ){
//                        override fun onMove(
//                            recyclerView: RecyclerView,
//                            viewHolder: RecyclerView.ViewHolder,
//                            target: RecyclerView.ViewHolder
//                        ): Boolean {
//                            return true
//                        }
//
//                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                            val pos = viewHolder.adapterPosition
//
//
////                if(adapter.getItemCount()>0){
//
//                        }
//
//                    }
//                    // attach swipe to recycler view
//                    ItemTouchHelper(itemTouchHelperCallbacks).apply {
//                        attachToRecyclerView(binding.messageRc)
//                    }
