package com.rony1533.ronyzap.activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.rony1533.ronyzap.R
import com.rony1533.ronyzap.adapter.UserAdapter
import com.rony1533.ronyzap.databinding.ActivityUsersBinding
import com.rony1533.ronyzap.firebase.FirebaseService
import com.rony1533.ronyzap.model.News
import com.rony1533.ronyzap.model.User

class UsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersBinding
    var userList = ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstallations.getInstance().id.addOnSuccessListener {
            FirebaseService.token = it
        }

        binding.userRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL , false)

        binding.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding.imgProfile.setOnClickListener {
            val intent = Intent(this@UsersActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
        getUsersList()
        getNewsApp()
    }

    private fun getNewsApp() {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("News").child("update")

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val news = snapshot.getValue(News::class.java)

                if (!news!!.info.isEmpty()){
                    binding.lNewsApp.visibility = View.VISIBLE
                    binding.tvNewsApp.text = news.info
                    binding.tvLinkNewsApp.text = news.link
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getUsersList() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

//        var userId = firebase.uid
//        FirebaseMessaging.getInstance().subscribeToTopic("/topicos/$userId")

        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (dataSnapShot: DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue(User::class.java)

                    if (!user!!.userId.equals(firebase.uid) ) {
                        userList.add(user)
                    } else {
                        binding.tvNameUser.text = user.userName

                        if (user!!.profileImage == "") {
                            binding.imgProfile.setImageResource(R.drawable.profile_image)
                        } else {
                            binding.imgProfile.setImageResource(R.drawable.profile_image)
//                            Glide.with(this@UsersActivity).load(user.profileImage).into(binding.imgProfile)
                        }
                    }
                }
                binding.userRecyclerView.layoutManager = LinearLayoutManager(this@UsersActivity)
                binding.userRecyclerView.adapter = UserAdapter(this@UsersActivity, userList)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}