package com.example.dm.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.example.dm.R
import com.example.dm.activity.ChatActivity
import com.example.dm.databinding.ChatItemViewBinding
import com.example.dm.model.UserInfo

class ChatAdapter(var context: Context,var list: ArrayList<UserInfo>):RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    class ChatViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding: ChatItemViewBinding = ChatItemViewBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = (LayoutInflater.from(parent.context)).inflate(R.layout.chat_item_view,parent,false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        var user = list[position]
        Glide.with(context).load(user.imgUri).into(holder.binding.userImg)
        holder.binding.userName.text = user.name.toString()

        if(user.activeStatus=="online") {
            holder.binding.onlineStatus.visibility = View.VISIBLE
            holder.binding.offilneStatus.visibility = View.GONE
        }else {
            holder.binding.onlineStatus.visibility = View.GONE
            holder.binding.offilneStatus.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChatActivity::class.java)
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