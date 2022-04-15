package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var firestoreDB:FirebaseFirestore
    private var users: MutableList<User> = mutableListOf()
    private lateinit var adapter: MainAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val uidCurrent = FirebaseAuth.getInstance().uid ?: ""

        firestoreDB = FirebaseFirestore.getInstance()
        val userReference = firestoreDB
            .collection("users")
            .whereNotEqualTo("userid",uidCurrent)
            .limit(20)

        userReference.addSnapshotListener{snapshot, exception ->
            if (exception !=null || snapshot ==null){

                return@addSnapshotListener
            }
            var userAry = snapshot.toObjects(User::class.java)
            users.clear()
            users.addAll(userAry)
            adapter.notifyDataSetChanged()

        }

        users = mutableListOf()

        recyclerMain.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(this, users)
        recyclerMain.adapter = adapter

        logoutBtn.setOnClickListener {
            signoutFunction()
        }



    }

    private fun signoutFunction() {
        auth.signOut()
        val intent= Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}