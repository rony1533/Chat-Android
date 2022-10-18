package com.rony1533.ronyzap.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import com.rony1533.ronyzap.R
import com.rony1533.ronyzap.activity.ChatActivity
import com.rony1533.ronyzap.model.User
import org.w3c.dom.Text
import java.io.File

class UserAdapter(
    private val context: Context,
     private val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.txtUserName.text = user.userName
        holder.userStatus.text = user.status

        if (user.status == "Online"){
            holder.userStatus.setTextColor(Color.parseColor("#00a000"))
        }
        if (user.status == "Offline"){
            holder.userStatus.setTextColor(Color.parseColor("#ff4040"))
        }

        if (user.profileImage == "") {
            holder.imgUser.setImageResource(R.drawable.profile_image)
        }
        else {
            holder.imgUser.setImageResource(R.drawable.profile_image)
//            Glide.with(context).load(user.profileImage).into(holder.imgUser)
        }

        holder.layoutUser.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", user.userId)
            intent.putExtra("userName", user.userName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        val txtUserName: TextView = view.findViewById(R.id.tvUserName)
        val txtTemp: TextView = view.findViewById(R.id.temp)
        val imgUser: ImageView = view.findViewById(R.id.userImage)
        val layoutUser: LinearLayout = view.findViewById(R.id.layoutUser)
        val userStatus: TextView = view.findViewById(R.id.tvStatus)
    }
}