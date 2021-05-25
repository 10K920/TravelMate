package com.example.fpauthenticationstep.MessagingActivities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.example.fpauthenticationstep.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.user_last_message.view.*

// Users can view all the users they exchanged messages with other users in this activity
// Users can click on the user shown in the activity to start a messaging page
class CurrentMessages : AppCompatActivity() {

    lateinit var othersUIDList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        supportActionBar?.title = "CurrentMessages"

        messageList_recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))


        val uid = FirebaseAuth.getInstance().uid
        val db = FirebaseFirestore.getInstance()

        messageList_recyclerView.layoutManager = GridLayoutManager(this, 1)

        if (uid != null){
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    val data: MutableMap<String, Any>? = document.data
                    val curMessageList = data?.get("others") as ArrayList<String>
                    othersUIDList = curMessageList
                    messageList_recyclerView.adapter = MessagesAdapter(othersUIDList)
                }
        }

    }

    inner class MessagesAdapter(val userItemList:  ArrayList<String>): RecyclerView.Adapter<MessagesAdapter.CurrentMessageViewHolder>() {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MessagesAdapter.CurrentMessageViewHolder {
            val itemView = LayoutInflater.from(p0.context).inflate(R.layout.user_last_message, p0, false)
            return CurrentMessageViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return userItemList.size
        }

        override fun onBindViewHolder(p0: MessagesAdapter.CurrentMessageViewHolder, p1: Int) {
            val db = FirebaseFirestore.getInstance()
            val userItem = userItemList[p1]

            if (userItem != FirebaseAuth.getInstance().uid){
                db.collection("users").document(userItem)
                    .get()
                    .addOnSuccessListener {
                        val data: MutableMap<String, Any>? = it.data
                        val username = data?.get("username") as String
                        val picUri = data?.get("picUri") as String
                        val userEmail = data?.get("email") as String
                        val userUID = data?.get("uid") as String
                        val userPlace = data?.get("place") as String


                        Picasso.with(this@CurrentMessages).load(picUri).into(p0.imageItem)
                        p0.nameItem.text = username
                        p0.UIDItem.text = userUID
                        p0.placeItem.text = userPlace


                        p0.row.setOnClickListener(){
                            val intent = Intent(this@CurrentMessages, MessagingPage::class.java)
                            intent.putExtra("RECEIVER_NAME", p0.nameItem.text.toString())
                            intent.putExtra("RECEIVER_PIC", picUri)
                            intent.putExtra("RECEIVER_PLACE", p0.placeItem.text.toString())
                            intent.putExtra("RECEIVER_UID", p0.UIDItem.text.toString())
                            startActivity(intent)
                        }
                    }
            }
        }

        inner class CurrentMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val row = itemView
            var imageItem: ImageView = itemView.userLast_imageView
            var nameItem: TextView = itemView.usernameLast_textview
            var UIDItem: TextView = itemView.curReceiverUID_Text
            var placeItem: TextView = itemView.curReceiverPlace_Text

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.to_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.to_menu -> {
                val intent = Intent(this, com.example.fpauthenticationstep.PathActivities.Menu::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}