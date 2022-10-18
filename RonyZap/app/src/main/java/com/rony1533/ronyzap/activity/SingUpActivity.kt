package com.rony1533.ronyzap.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.rony1533.ronyzap.databinding.ActivitySingUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SingUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReferemce: DatabaseReference
    private lateinit var binding: ActivitySingUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnSingUp.setOnClickListener {
            val userName = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etPasswordC.text.toString()

            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(applicationContext, "username is required", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "email is required", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "password is required", Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(applicationContext, "confirm Password is required", Toast.LENGTH_SHORT).show()
            }
            if (password.length < 6) {
                Toast.makeText(applicationContext, "Password required minumum 6 characters", Toast.LENGTH_SHORT).show()
            }

            if (password != confirmPassword) {
                Toast.makeText(applicationContext, "password not match", Toast.LENGTH_SHORT).show()
            }
            registerUser(userName, email, password)
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@SingUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    val userId: String = user!!.uid

                    databaseReferemce = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap: HashMap<String, String> = HashMap()
                    hashMap.put("userId", userId)
                    hashMap.put("userName", userName)
                    hashMap.put("profileImage", "")

                    databaseReferemce.setValue(hashMap).addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            binding.etName.setText("")
                            binding.etEmail.setText("")
                            binding.etPassword.setText("")
                            binding.etPasswordC.setText("")
                            val intent = Intent(this@SingUpActivity, UsersActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
    }
}