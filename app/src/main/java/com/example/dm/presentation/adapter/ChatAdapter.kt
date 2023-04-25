package com.example.dm.presentation.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dm.R
import com.example.dm.presentation.activity.ChatActivity
import com.example.dm.databinding.ChatItemViewBinding
import com.example.dm.presentation.data.model.Message
import com.example.dm.presentation.data.model.UserInfo
import com.example.dm.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatAdapter(var context: Context,var list: List<UserInfo>):RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    lateinit var database: FirebaseDatabase
    class ChatViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding: ChatItemViewBinding = ChatItemViewBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = (LayoutInflater.from(parent.context)).inflate(R.layout.chat_item_view,parent,false)
        return ChatViewHolder(view)
    }



    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        var lastMessage = ""
        var user = list[position]
        Glide.with(context).load(user.imgUri)
            .thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.userImg)
        holder.binding.userName.text = user.name

        database = FirebaseUtils.firebaseDatabase
        database.reference
            .child("chats")
            .child(FirebaseUtils.firebaseAuth.uid+user.uid.toString())
            .child("message")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot1 in snapshot.children) {
                        val data = snapshot1.getValue(Message::class.java)
                        lastMessage = data!!.message
                        println("mujeeb $lastMessage")
                        holder.binding.lastMessage.text = lastMessage.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        if(user.activeStatus=="online") {
            holder.binding.onlineStatus.visibility = View.VISIBLE
            holder.binding.offilneStatus.visibility = View.GONE
        }else {
            holder.binding.onlineStatus.visibility = View.GONE
            holder.binding.offilneStatus.visibility = View.VISIBLE
        }


        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("active",user.activeStatus)
            intent.putExtra("uid", user.uid)
            intent.putExtra("name",user.name)
            intent.putExtra("img",user.imgUri)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}

