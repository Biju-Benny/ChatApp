package com.example.chatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.received_msg.view.*
import kotlinx.android.synthetic.main.sent_msg.view.*

class ChatAdapter( val context: Context,val messageList: ArrayList<Message>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    class SentViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){

        val sentMessage = itemView.text_sent_message


    }

    class ReceiveViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val receivedMessage = itemView.text_receive_message

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1){
            //inflate Receive
            val view = LayoutInflater.from(context).inflate(R.layout.received_msg, parent, false)
            return ReceiveViewHolder(view)
        }else{
            //inflate sent

            val view = LayoutInflater.from(context).inflate(R.layout.sent_msg, parent, false)
            return SentViewHolder(view)
        }

    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if (holder.javaClass ==SentViewHolder::class.java){
            // do the stuff for sent view holder


            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        }else{
            //do stuff for the receive view holder
            val viewHolder = holder as ReceiveViewHolder
            holder.receivedMessage.text = currentMessage.message
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return  ITEM_RECEIVE
        }    }

    override fun getItemCount(): Int = messageList.size
}