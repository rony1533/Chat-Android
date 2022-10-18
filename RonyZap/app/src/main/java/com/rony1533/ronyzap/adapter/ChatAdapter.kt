package com.rony1533.ronyzap.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.rony1533.ronyzap.R
import com.rony1533.ronyzap.model.Chat
import com.rony1533.ronyzap.model.User

class ChatAdapter(private val context: Context, private val chatList: ArrayList<Chat>):
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    var firebaseUser:FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
            return ViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
            return ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList[position]
        holder.txtUserMessage.text = chat.message
        holder.txtUserName.text = chat.userName
        holder.txtData.text = chat.date
//        Glide.with(context).load(holder.imgUser).placeholder(R.drawable.profile_image).into(holder.imgUser)

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    class  ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtUserMessage: TextView = view.findViewById(R.id.tvMessage)
        val txtUserName: TextView = view.findViewById(R.id.tvUserName)
        val txtData: TextView = view.findViewById(R.id.tvDate)
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if(chatList[position].senderId == firebaseUser!!.uid){
            return MESSAGE_TYPE_RIGHT
        } else {
            return MESSAGE_TYPE_LEFT
        }
    }

}