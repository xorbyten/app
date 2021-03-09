package com.example.app.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.app.R
import com.example.app.words_db.DBHandler

class WelcomeActivity : Activity() {

    lateinit var button: Button
    lateinit var et_username: EditText
    lateinit var et_password: EditText
    lateinit var tv_signup: TextView
    lateinit var dbHandler: DBHandler

    val REQUEST_ID_MULTIPLE_PERMISSIONS: Int = 1
    val PERMISSIONS_LIST: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        checkAndRequestPermissions()

        button = findViewById(R.id.btn_OK)
        et_username = findViewById(R.id.et_username)
        et_password = findViewById(R.id.et_password)
        tv_signup = findViewById(R.id.tv_signup_now)

        dbHandler = DBHandler(this)

        button.setOnClickListener {

            if(dbHandler.checkUser(et_username.text.toString())) {
                if(et_username.text.isEmpty() && et_password.text.isEmpty()) {
                    et_username.error = "The field is empty"
                    et_password.error = "The field is empty"
                } else if (et_username.text.isEmpty()) {
                    et_username.error = "The field is empty"
                } else if(et_password.text.isEmpty()) {
                    et_password.error = "The field is empty"
                } else {
                    val sharedPref = this.getSharedPreferences("user_login_pref", Context.MODE_PRIVATE)
                    val editorPref = sharedPref.edit()
                    editorPref.putString("username", et_username.text.toString())
                    editorPref.apply()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "This user doesn't exists. Please Signup.", Toast.LENGTH_SHORT).show()
            }
        }

        val sharedPreferences = getSharedPreferences("user_login_pref", MODE_PRIVATE)
        if(sharedPreferences.contains("username")) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        tv_signup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun checkAndRequestPermissions(): Boolean {
        val permission_write_external_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission_read_external_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val permission_get_accounts = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)

        if(permission_write_external_storage != PackageManager.PERMISSION_GRANTED)
            PERMISSIONS_LIST.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(permission_read_external_storage != PackageManager.PERMISSION_GRANTED)
            PERMISSIONS_LIST.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        if(permission_get_accounts != PackageManager.PERMISSION_GRANTED)
            PERMISSIONS_LIST.add(Manifest.permission.GET_ACCOUNTS)
        if(!PERMISSIONS_LIST.isEmpty()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_LIST.toArray(arrayOf(PERMISSIONS_LIST.size.toString())), REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i("res", "${grantResults[0]}")
        if(requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if(grantResults.isNotEmpty()) {
                for(i in 0 until PERMISSIONS_LIST.size) {
                    if(PERMISSIONS_LIST[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED)
                            Log.i("PermissionMsg", "Write External Storage Permission Granted.")
                    if(PERMISSIONS_LIST[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE))
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED)
                            Log.i("PermissionMsg", "Read External Storage Permission Granted.")
                    if(PERMISSIONS_LIST[i].equals(Manifest.permission.GET_ACCOUNTS))
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED)
                            Log.i("PermissionMsg", "Get Accounts Permission Granted.")
                }
            }
        }
    }
}