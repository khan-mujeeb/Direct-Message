package com.example.dm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.dm.R
import com.example.dm.databinding.ReceverItemViewBinding
import com.example.dm.databinding.SentItemViewBinding
import com.example.dm.model.Message
import com.example.dm.utils.FirebaseUtils

class MessageAdapter(var context: Context, var list: ArrayList<Message>): RecyclerView.Adapter<ViewHolder>() {
    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ITEM_SENT)
            SentViewHolder(LayoutInflater.from(context).inflate(R.layout.sent_item_view,parent,false))
        else
            ReceiverViewHolder(LayoutInflater.from(context).inflate(R.layout.recever_item_view,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var message = list[position]
        if(holder.itemViewType == ITEM_SENT) {
            val viewHolder = holder as SentViewHolder
            viewHolder.binding.textSend.text = message.message
        }else {
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.binding.textReceive.text = message.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(FirebaseUtils.firebaseAuth.uid == list[position].sendUid)
            ITEM_SENT
        else
            ITEM_RECEIVE
    }

    inner class SentViewHolder(view: View): ViewHolder(view) {
        var binding = SentItemViewBinding.bind(view)
    }

    inner class ReceiverViewHolder(view: View): ViewHolder(view) {
        var binding = ReceverItemViewBinding.bind(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}