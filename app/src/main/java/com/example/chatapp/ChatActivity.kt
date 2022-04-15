package com.example.chatapp

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    val db = Firebase.firestore

    private var messageList: ArrayList<Message> = ArrayList()
    var receiverRoom: String? = null
    var senderRoom: String? = null
    private lateinit var adapter: ChatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val receiverName =intent.getStringExtra("userName")
        val receiverId = intent.getStringExtra("userId")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid


        senderRoom = receiverId + senderUid
        receiverRoom = senderUid + receiverId



        supportActionBar?.title = receiverName

        send_btn.setOnClickListener {

            val message = message_send_edit_text.text.toString()
            val currentTime = System.currentTimeMillis()
            val messageObject = Message(message,senderUid,currentTime.toString())
            db.collection("chats").document(senderRoom!!).collection("messages").document()
                .set(messageObject)
                .addOnSuccessListener {
                    db.collection("chats").document(receiverRoom!!).collection("messages").document()
                        .set(messageObject)
                }
            message_send_edit_text.setText("")



        }




        //add data to recyclerview

        val msgRef = db.collection("chats").document(senderRoom!!).collection("messages")
            .orderBy("createdTime",Query.Direction.ASCENDING)

        msgRef.addSnapshotListener{ snapshot, exception ->
            if (exception !=null || snapshot ==null){
                return@addSnapshotListener
            }
            var msgArray = snapshot.toObjects(Message::class.java)
            messageList.clear()
            messageList.addAll(msgArray)
            adapter.notifyDataSetChanged()

        }

        chatRecycler.layoutManager = LinearLayoutManager(this)
        adapter = ChatAdapter(this,messageList)
        chatRecycler.adapter = adapter














    }
}