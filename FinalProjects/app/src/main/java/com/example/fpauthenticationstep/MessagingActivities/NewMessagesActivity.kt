package com.example.fpauthenticationstep.MessagingActivities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.fpauthenticationstep.MapActivities.MapsActivity
import com.example.fpauthenticationstep.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_new_messages.*
import kotlinx.android.synthetic.main.user_row_new_messages.view.*

// this page is pulled up when a user choose on a location nearby and decide to view other users also interested in the place
// users can press on pictures to choose a user to send messages to
class NewMessagesActivity() : AppCompatActivity() {

    private lateinit var curPlace: String
    private var sameChecked: Boolean = false
    var userUserItem: ArrayList<UserItem> = ArrayList()
    var userSameUserItem: ArrayList<UserItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_messages)

        supportActionBar?.title = "Select User"

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().uid
        curPlace = intent.extras!!.getSerializable("CURPLACE") as String

        recyclerview_newmessages.layoutManager = GridLayoutManager(this, 2)

        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d("MessagingPage", "DocumentSnapshot data: ${document.data}")
                    }
                    else {
                        Log.d("MessagingPage", "No such document")
                    }

                    val data: MutableMap<String, Any>? = document.data
                    val nationality = data?.get("nationality") as String
                    val curUserNationality = nationality

                    db.collection("users").whereEqualTo("place", curPlace).whereEqualTo("nationality", curUserNationality)
                        .get()
                        .addOnSuccessListener {result ->

                            for (document in result) {
                                val data: MutableMap<String, Any>? = document.data
                                val username = data?.get("username") as String
                                val picUri = data?.get("picUri") as String
                                val userEmail = data?.get("email") as String
                                val userUID = data?.get("uid") as String
                                val userPlace = data?.get("place") as String

                                val userItem = UserItem(
                                    this@NewMessagesActivity,
                                    username,
                                    picUri,
                                    userEmail,
                                    userUID,
                                    userPlace
                                )

                                if (userUID != FirebaseAuth.getInstance().uid){
                                    userSameUserItem.add(userItem)
                                }

                            }

                        }
                }
        }

        db.collection("users").whereEqualTo("place", curPlace)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data: MutableMap<String, Any>? = document.data
                    val username = data?.get("username") as String
                    val picUri = data?.get("picUri") as String
                    val userEmail = data?.get("email") as String
                    val userUID = data?.get("uid") as String
                    val userPlace = data?.get("place") as String

                    val userItem = UserItem(
                        this@NewMessagesActivity,
                        username,
                        picUri,
                        userEmail,
                        userUID,
                        userPlace
                    )
                    if (userUID != FirebaseAuth.getInstance().uid){
                        userUserItem.add(userItem)
                    }

                }

                Log.d("ListCheck", "userMessageItem contains: $userUserItem")
                recyclerview_newmessages.adapter = NewMessagesAdapter(userUserItem)
            }

        viewMap_btn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        fromSameCheck.setOnClickListener {
            if (sameChecked == true){
                recyclerview_newmessages.adapter = NewMessagesAdapter(userUserItem)
                sameChecked = false
                fromSameCheck.text = "Same Country"
            }
            else {
                recyclerview_newmessages.adapter = NewMessagesAdapter(userSameUserItem)
                sameChecked = true
                fromSameCheck.text = "All Users"
            }
        }
    }

    inner class NewMessagesAdapter(val userItemList:  ArrayList<UserItem>): RecyclerView.Adapter<NewMessagesAdapter.NewMessageViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NewMessagesAdapter.NewMessageViewHolder {
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.user_row_new_messages, p0, false)
            return NewMessageViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return userItemList.size
        }

        override fun onBindViewHolder(p0: NewMessagesAdapter.NewMessageViewHolder, p1: Int) {

            val userItem = userItemList[p1]
            val picUri = userItem.picLoc
            val username = userItem.username
            val userEmail = userItem.email
            val userUID = userItem.UID
            val userPlace = userItem.place

            Picasso.with(this@NewMessagesActivity).load(picUri).into(p0.imageItem)

            p0.row.setOnClickListener(){
                val intent = Intent(this@NewMessagesActivity, MessagingPage::class.java)
                intent.putExtra("RECEIVER_NAME", username)
                intent.putExtra("RECEIVER_PIC", picUri)
                intent.putExtra("RECEIVER_EMAIL", userEmail)
                intent.putExtra("RECEIVER_UID", userUID)
                intent.putExtra("RECEIVER_PLACE", userPlace)
                startActivity(intent)
            }
        }

        inner class NewMessageViewHolder(itenView: View): RecyclerView.ViewHolder(itenView) {
            val row = itemView
            var imageItem: ImageView = itemView.user_imageView
        }

    }
}
