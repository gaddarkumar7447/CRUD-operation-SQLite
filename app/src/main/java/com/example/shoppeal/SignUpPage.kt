package com.example.shoppeal

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class SignUpPage : AppCompatActivity() {
    lateinit var firebaseauth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.statusBar)
        val msingupemail = findViewById<EditText>(R.id.signupemail)
        val msinguppassword = findViewById<EditText>(R.id.signuppassword)
        val msignup = findViewById<TextView>(R.id.signup)
        findViewById<TextView>(R.id.gotologin).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        })

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(R.string.default_web_client_id.toString()).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<CardView>(R.id.signInWithFaceBook).setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Integrating Facebook Sign Up Soon...", Toast.LENGTH_SHORT).show()
        })
        findViewById<CardView>(R.id.signInWithGoogle).setOnClickListener(View.OnClickListener {
            signInWithGoogle()
        })
        firebaseauth = FirebaseAuth.getInstance();

        msignup.setOnClickListener(View.OnClickListener {
            val email = msingupemail.text.toString().trim()
            val password = msinguppassword.text.toString().trim()
            if (email.isEmpty()) msingupemail.error = "Email empty"
            else if (password.isEmpty()) msinguppassword.error = "Password empty"
            else if (password.length < 7) msinguppassword.error =
                "password should greater than 7 digit"
            else {
                firebaseauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT)
                                .show()
                            sendEmailVerification()
                        } else {
                            Toast.makeText(this, "Failed to register", Toast.LENGTH_SHORT).show()
                        }
                })
            }
        })
    }

    private fun sendEmailVerification() {
        val firebaseUser: FirebaseUser? = firebaseauth.currentUser
        firebaseUser?.sendEmailVerification()?.addOnCompleteListener {
            Toast.makeText(
                this,
                "Verification Email is send, Verify and login again",
                Toast.LENGTH_SHORT
            ).show()
            firebaseauth.signOut()
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
            ?: Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show()
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        /*launcher.launch(signInIntent)*/
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseauth.signInWithCredential(credential).addOnCompleteListener(
                OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, Employee::class.java))
                        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseauth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseauth.currentUser
                } else {

                }
            }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { results ->
            if (results.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(results.data)
                handleResult(task)
            }
        }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
                /* firebaseAuthWithGoogle(account.idToken!!)*/
            } else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseauth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "successful", Toast.LENGTH_SHORT).show()
                Log.d("Tag", "Successful")
            } else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val RC_SIGN_IN = 1001
        const val EXTRA_NAME = "EXTRA_NAME"
    }
}

