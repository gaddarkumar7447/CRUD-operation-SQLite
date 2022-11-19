package com.example.shoppeal.signInPage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.shoppeal.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class ForgetPassword : AppCompatActivity() {
    lateinit var firebaseauth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.statusBar)
        val mforgetPassword = findViewById<EditText>(R.id.forgetpassword);
        val mpassportRecoverButton = findViewById<TextView>(R.id.passwordrecoverbutton)

        findViewById<TextView>(R.id.gobacktologin).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        })
        firebaseauth = FirebaseAuth.getInstance()
        mpassportRecoverButton.setOnClickListener {
            val mail = mforgetPassword.text.toString().trim()
            if (mail.isEmpty()) {
                mforgetPassword.error = "email is empty"
            } else {
                firebaseauth.sendPasswordResetEmail(mail).addOnCompleteListener(OnCompleteListener<Void?> { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@ForgetPassword, "Mail send u can recover your password",Toast.LENGTH_SHORT).show()
                            finish()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            mforgetPassword.error = "Mail not exits"
                        }
                    })
            }
        }
    }
    override fun onBackPressed() {
        super.finish()
    }
}