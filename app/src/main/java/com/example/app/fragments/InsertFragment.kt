package com.example.app.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.app.R
import com.example.app.activities.MainActivity
import com.example.app.words_db.DBHandler

class InsertFragment : Fragment() {

    lateinit var et_addFront: EditText
    lateinit var et_addBack: EditText
    lateinit var bt_ok: Button
    lateinit var dbHandler: DBHandler

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.insert_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        et_addFront = view.findViewById(R.id.et_front)
        et_addBack = view.findViewById(R.id.et_back)
        bt_ok = view.findViewById(R.id.btn_insert)

        dbHandler = DBHandler(activity!!.applicationContext)

        bt_ok.setOnClickListener {
            val sharedPreferences: SharedPreferences = activity!!.getSharedPreferences("user_login_pref", Context.MODE_PRIVATE)
            val name_tag = sharedPreferences.getString("username", "")
            Log.i("TAG1", "Name Tag = $name_tag")
            dbHandler.insert_word(et_addFront.text.toString(), et_addBack.text.toString(), name_tag)
            et_addFront.text.clear()
            et_addBack.text.clear()
            (activity as MainActivity).display_entries()
            Toast.makeText(activity, "Word added successfuly.", Toast.LENGTH_SHORT).show()
        }
    }
}