package com.example.shoppeal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Employee : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_employee)

    }
}