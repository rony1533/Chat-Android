package com.rony1533.ronyzap.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rony1533.ronyzap.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null
    private lateinit var binding: com.rony1533.ronyzap.databinding.ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser

        if ( firebaseUser != null) {
            val intent = Intent(this@LoginActivity, UsersActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "email and password are required", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            binding.etEmail.setText("")
                            binding.etPassword.setText("")
                            val intent = Intent(this@LoginActivity, UsersActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else {
                            Toast.makeText(applicationContext, "email or password invalid", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        binding.btnSingUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SingUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}