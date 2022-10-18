package com.rony1533.ronyzap.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.rony1533.ronyzap.R
import com.rony1533.ronyzap.adapter.ChatAdapter
import com.rony1533.ronyzap.databinding.ActivityChatBinding
import com.rony1533.ronyzap.model.Chat
import com.rony1533.ronyzap.model.PushNotification
import com.rony1533.ronyzap.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {
    var firebaseUser:FirebaseUser? = null
    var reference:DatabaseReference? = null
    private lateinit var databaseReference: DatabaseReference
    var chatList = ArrayList<Chat>()
    var topic = ""
    var name = ""

    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL , false)

        var userId = intent.getStringExtra("userId")
        var userName = intent.getStringExtra("userName")

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                name = user!!.userName

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                binding.tvUserName.text = user!!.userName
                binding.tvStatus.text = user.status

                if (user.status == "Online"){
                    binding.tvStatus.setTextColor(Color.parseColor("#00a000"))
                }
                if (user.status == "Offline"){
                    binding.tvStatus.setTextColor(Color.parseColor("#ff4040"))
                }

                if (user.profileImage == "") {
                    binding.imgProfile.setImageResource(R.drawable.profile_image)
                } else {
                    binding.imgProfile.setImageResource(R.drawable.profile_image)
//                    Glide.with(this@ChatActivity).load(user.profileImage).into(binding.imgProfile)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.btnSendMessage.setOnClickListener {
            var message: String = binding.etMessage.text.toString()
            val currentTime = Calendar.getInstance().time.hours
            val currentTimae = Calendar.getInstance().time.seconds
            val dateTimeNow = "$currentTime:$currentTimae"

            if (message.isEmpty()){
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                binding.etMessage.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, userId, name!!, message, dateTimeNow)
                binding.etMessage.setText("")
//                topic = "/topics/$userId"
//                PushNotification(NotificationData(userName!!, message), topic)
//                    .also { sendNotification(it) }
            }

        }

        readMessage(firebaseUser!!.uid, userId)
    }

    private fun sendMessage(senderId: String, receiverId: String, NameSender: String, message: String, dateTime: String) {
        var reference:DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiveId", receiverId)
        hashMap.put("userName", NameSender)
        hashMap.put("message", message)
        hashMap.put("date", dateTime)

        reference!!.child("Chat").push().setValue(hashMap)
    }

    private fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()

                for (dataSnapShot: DataSnapshot in snapshot.children){
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.receiveId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiveId.equals(senderId)) {
                        chatList.add(chat)
                    }
                }

                val userAdapter = ChatAdapter(this@ChatActivity, chatList)

                binding.chatRecyclerView.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
//        try {
//            val response = RetrofitInstance.api.postNotification(notification)
//            if (response.isSuccessful) {
//                Toast.makeText(this@ChatActivity, "Response ${Gson().toJson(response)}", Toast.LENGTH_SHORT).show()
//            }
//            else {
//                Toast.makeText(this@ChatActivity, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
//            }
//        }
//        catch (e:java.lang.Exception) {
//            Toast.makeText(this@ChatActivity, e.message, Toast.LENGTH_SHORT).show()
//        }
    }

}