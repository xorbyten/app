package com.example.app.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import android.view.ActionMode
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.CustomAdapter
import com.example.app.MyDetailsLookup
import com.example.app.words_db.DataSource
import com.example.app.R
import com.example.app.fragments.InsertFragment
import com.example.app.words_db.DBHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), CustomAdapter.myLongClickListener {

    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var rv_allWords: RecyclerView
    lateinit var fab: FloatingActionButton
    lateinit var adapter: CustomAdapter
    lateinit var dbHandler: DBHandler
    lateinit var dataSource: DataSource
    lateinit var exampleList: ArrayList<DataSource>
    var tracker: SelectionTracker<Long>? = null
    var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DBHandler(this)
        dataSource = DataSource(0, "0", "0", "0")
        
        rv_allWords = findViewById(R.id.rv_allWords) as RecyclerView
        fab = findViewById(R.id.fab)

        linearLayoutManager = LinearLayoutManager(this)
        rv_allWords.layoutManager = linearLayoutManager
        rv_allWords.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))

        display_entries()
        adapter.notifyDataSetChanged()

        fab.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, InsertFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        tracker = SelectionTracker.Builder<Long>(
                "mySelection",
                rv_allWords,
                StableIdKeyProvider(rv_allWords),
                MyDetailsLookup(rv_allWords),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build()

        adapter.tracker = tracker
    }

    override fun myLongClick(dataSource: DataSource, position: Int): Boolean {
        Toast.makeText(this, "Click at = ${position}", Toast.LENGTH_SHORT).show()
        actionMode = this.startActionMode(ActionModeCallback)

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId

        when(id) {
            R.id.mn_logout -> {
                val sharedPreferences: SharedPreferences = getSharedPreferences("user_login_pref", Context.MODE_PRIVATE)
                val sharedEditor: SharedPreferences.Editor = sharedPreferences.edit()
                sharedEditor.remove("username")
                sharedEditor.apply()
                finish()
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            }
            R.id.mn_exit -> {
                System.exit(0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun display_entries() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_login_pref", Context.MODE_PRIVATE)
        val name_tag = sharedPreferences.getString("username", "")
        exampleList = dbHandler.get_words_by_tag(name_tag)
        adapter = CustomAdapter()
        adapter.setData(exampleList, this)
        rv_allWords.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private val ActionModeCallback = object: ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            val inflater: MenuInflater = mode!!.menuInflater
            inflater.inflate(R.menu.context_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when(item!!.itemId) {
                R.id.cnt_delete_row -> {
                    Toast.makeText(applicationContext, "Delete Clicked!", Toast.LENGTH_SHORT).show()
                    return true
                }
                R.id.cnt_diselect_all -> {
                    Toast.makeText(applicationContext, "Diselect Clicked!", Toast.LENGTH_SHORT).show()
                    return true
                }
            }
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
        }
    }
}