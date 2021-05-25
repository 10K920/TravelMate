package com.example.fpauthenticationstep.MessagingActivities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fpauthenticationstep.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_messaging_page.*
import kotlinx.android.synthetic.main.row_from.view.*
import kotlinx.android.synthetic.main.row_to.view.*

// the actual chatting page
// when user send a message to another user for the first time, each others uid is stored in each others collection
// user is prohibited from sending messages when the receiver is no longer interested in the same place
class MessagingPage : AppCompatActivity() {

    private var curUserPlace: String = ""

    private lateinit var curReceiverName: String
    private lateinit var curReceiverPlace: String
    private lateinit var curReceiverUID: String
    private lateinit var senderMessageList: ArrayList<String>
    private lateinit var getterMessageList: ArrayList<String>

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging_page)



        messageLog_View.adapter = adapter

        curReceiverName = intent.extras!!.getSerializable("RECEIVER_NAME") as String
        curReceiverPlace = intent.extras!!.getSerializable("RECEIVER_PLACE") as String
        curReceiverUID = intent.extras!!.getSerializable("RECEIVER_UID") as String

        supportActionBar?.title = curReceiverName

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().uid

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
                    val place = data?.get("place") as String
                    val messageList = data?.get("others") as ArrayList<String>
                    curUserPlace = place
                    senderMessageList = messageList
                }
        }

        getMessages()

        send_btn.setOnClickListener {
            Log.d("MessagePage", "Attempt to send message")

            if (curUserPlace != curReceiverPlace) {
                Toast.makeText(this@MessagingPage, "You can only send message to users in the same place", Toast.LENGTH_LONG).show()
            }
            else {
                sendMessage()
            }

        }
    }


    private fun getMessages() {
        val fromUID = FirebaseAuth.getInstance().uid
        val toUID = curReceiverUID

        val ref = FirebaseDatabase.getInstance().getReference("/messages/$fromUID/$toUID")

        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val inputMessage = p0.getValue(InputMessage::class.java)

                if (inputMessage != null) {
                    Log.d("MessagePage", inputMessage?.text)

                    if (inputMessage.fromUID == FirebaseAuth.getInstance().uid.toString()){
                        adapter.add(
                            MessageFromItem(
                                this@MessagingPage,
                                inputMessage.text
                            )
                        )
                    } else{
                        adapter.add(
                            MessageToItem(
                                this@MessagingPage,
                                inputMessage.text,
                                curReceiverName
                            )
                        )
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }

    private fun sendMessage() {
        val curTextInput = typeMessage_editText.text.toString()
        val db = FirebaseFirestore.getInstance()
        val fromUID = FirebaseAuth.getInstance().uid.toString()
        val toUID = curReceiverUID

        val fromReference = FirebaseDatabase.getInstance().getReference("/messages/$fromUID/$toUID").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/messages/$toUID/$fromUID").push()

        val curSending = InputMessage(
            fromReference.key!!,
            curTextInput,
            fromUID,
            toUID,
            System.currentTimeMillis() / 1000
        )

        fromReference.setValue(curSending)
            .addOnSuccessListener {
                Log.d("MessagePage", "CurrentMessages successfully saved: ${fromReference.key}")
                typeMessage_editText.text.clear()
                messageLog_View.scrollToPosition(adapter.itemCount - 1)
            }

        toReference.setValue(curSending)

        if (curReceiverUID != null) {
            db.collection("users").document(curReceiverUID).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d("MessagingPage", "DocumentSnapshot data: ${document.data}")
                    }
                    else {
                        Log.d("MessagingPage", "No such document")
                    }

                    val data: MutableMap<String, Any>? = document.data
                    val place = data?.get("place") as String
                    val messageList = data?.get("others") as ArrayList<String>
                    curUserPlace = place
                    getterMessageList = messageList

                    if (!senderMessageList.contains(toUID)){
                        senderMessageList.add(toUID)
                        getterMessageList.add(fromUID)
                        if (fromUID != null){
                            db.collection("users").document(fromUID).update("others", senderMessageList)
                            db.collection("users").document(toUID).update("others", getterMessageList)
                        }

                    }

                }
        }


    }
}

class MessageFromItem(val context: Context, val text_from: String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.text_from.text = text_from
    }

    override fun getLayout(): Int {
        return R.layout.row_from
    }
}

class MessageToItem(val context: Context, val text_to: String, val name: String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.text_to.text = text_to
        viewHolder.itemView.curReceiveName_text.text = name
    }

    override fun getLayout(): Int {
        return R.layout.row_to
    }
}
