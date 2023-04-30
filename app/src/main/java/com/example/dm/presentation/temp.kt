package com.example.dm.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.dm.R
import com.example.dm.presentation.activity.MainActivity

class temp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp)

        val x = findViewById<TextView>(R.id.btn)
        x.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}