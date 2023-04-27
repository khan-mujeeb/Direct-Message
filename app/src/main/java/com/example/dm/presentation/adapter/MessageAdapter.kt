package com.example.dm.presentation.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.dm.R
import com.example.dm.databinding.ReceverItemViewBinding
import com.example.dm.databinding.SentItemViewBinding
import com.example.dm.data.model.Message
import com.example.dm.data.viewmodel.ViewModel
import com.example.dm.utils.ConstUtils.cancel
import com.example.dm.utils.FirebaseUtils

class MessageAdapter(
    var context: Context,
    var list: ArrayList<Message>,
    val senderRoom: String,
    val reciverRoom: String,
    val viewModel: ViewModel
): RecyclerView.Adapter<ViewHolder>() {


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
        val type = holder.itemViewType
        if(type == ITEM_SENT) {
            val viewHolder = holder as SentViewHolder
            viewHolder.binding.textSend.text = message.message
        }else {
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.binding.textReceive.text = message.message
        }


        holder.itemView.setOnLongClickListener {
            if (type == ITEM_SENT) {
                val builder = AlertDialog.Builder(holder.itemView.context)
                builder.setMessage("Do you want to delete this message?")

                builder.setNegativeButton("Delete for everyone") { _, _ ->

                    viewModel.deleteSenderMessage(
                        senderRoom = senderRoom,
                        messageId = message.messageId
                    )

                    viewModel.deleteReciverMessage(
                        reciverRoom = reciverRoom,
                        messageId = message.messageId
                    )
                    notifyItemRemoved(position)

                }

                builder.setPositiveButton("Delete for me") { _, _ ->
                    viewModel.deleteSenderMessage(
                        senderRoom = senderRoom,
                        messageId = message.messageId
                    )
                    notifyItemRemoved(position)
                }


                builder.setNeutralButton(cancel) { _, _ ->

                }
                builder.create().show()
                true
            } else {
                val builder = AlertDialog.Builder(holder.itemView.context)
                builder.setMessage("Do you want to delete this message?")
                builder.setPositiveButton("Delete for me") { _, _ ->
                    viewModel.deleteSenderMessage(
                        senderRoom = senderRoom,
                        messageId = message.messageId
                    )
                }
                builder.setNegativeButton(cancel) { _, _ ->
                }
                builder.create().show()
                true
            }
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