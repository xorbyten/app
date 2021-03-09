package com.example.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.app.R
import com.example.app.words_db.DBHandler

class RegisterActivity : Activity() {

    lateinit var btn_signup: Button
    lateinit var et_username: EditText
    lateinit var et_password: EditText
    lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_signup = findViewById(R.id.btn_signup)
        et_username = findViewById(R.id.et_username)
        et_password = findViewById(R.id.et_password)

        dbHandler = DBHandler(this)

        btn_signup.setOnClickListener {

            if (et_username.text.isEmpty() && et_password.text.isEmpty()) {
                et_username.error = "The field is empty"
                et_password.error = "The field is empty"
            } else if (et_username.text.isEmpty()) {
                et_username.error = "The field is empty"
            } else if(et_password.text.isEmpty()) {
                et_password.error = "The field is empty"
            } else if(dbHandler.checkUser(et_username.text.toString())) {
                Toast.makeText(this, "User already exists.", Toast.LENGTH_LONG).show()
            } else {
                dbHandler.insert_user(et_username.text.toString(), et_password.text.toString())
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}