package com.example.exchangeabletoken

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        // add Listener for the buttons
        findViewById<Button>(R.id.proceed_button).setOnClickListener {
            val intent = Intent(this, ChooseSignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
