package com.example.shoppeal.signInPage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.shoppeal.Employee
import com.example.shoppeal.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.statusBar)
        val mloginemail = findViewById<EditText>(R.id.loginemail)
        val mloginpassword = findViewById<EditText>(R.id.loginpassword)
        val mlogin = findViewById<TextView>(R.id.login)
        progressBar = findViewById(R.id.progressbarinmainactivity)
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            finish()
            startActivity(Intent(this, Employee::class.java))
        }
        findViewById<TextView>(R.id.gotosingup).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SignUpPage::class.java))
        })
        findViewById<TextView>(R.id.gotoforgetpassword).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
        })

        mlogin.setOnClickListener {
            val email = mloginemail.text.toString().trim()
            val password = mloginpassword.text.toString().trim()
            if (email.isEmpty()) {
                mloginemail.error = "Email is empty"
            } else if (password.isEmpty()) {
                mloginpassword.error = "password is empty"
            } else {
                progressBar.visibility = View.VISIBLE
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            checkMainVerification()
                        } else {
                            mloginemail.error = "Email is not exits"
                            mloginpassword.error = "password is not exits"
                            progressBar.visibility = View.INVISIBLE
                        }
                    }
            }
        }
    }

    private fun checkMainVerification() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser!!.isEmailVerified) {
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
            finish()
            startActivity(Intent(this, Employee::class.java))
        } else {
            progressBar.visibility = View.INVISIBLE
            Toast.makeText(this, "Verify email first", Toast.LENGTH_SHORT).show()
            firebaseAuth.signOut()
        }
    }
}
