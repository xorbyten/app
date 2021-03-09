package com.example.app.words_db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private val DB_NAME = "words_db"
private var DB_VERSION = 1

class DBHandler(context: Context) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    private var USER_TABLE_NAME = "user_table"
    private var USER_KEY_ID = "id"
    private var USER_KEY_UNAME = "uname"
    private var USER_KEY_PSW = "password"

    private var WORDS_TABLE_NAME = "words_table"
    private var WORDS_KEY_ID = "id"
    private var WORDS_KEY_FRONT = "front"
    private var WORDS_KEY_BACK = "back"
    private var WORDS_KEY_TAG = "tag"

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USER_TABLE = ("CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME + "("
                + USER_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_KEY_UNAME + " TEXT,"
                + USER_KEY_PSW + " TEXT" + ")")
        val CREATE_WORDS_TABLE = ("CREATE TABLE IF NOT EXISTS " + WORDS_TABLE_NAME + "("
                + WORDS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + WORDS_KEY_FRONT + " TEXT,"
                + WORDS_KEY_BACK + " TEXT," + WORDS_KEY_TAG + " TEXT" + ")")
        db?.execSQL(CREATE_USER_TABLE)
        db?.execSQL(CREATE_WORDS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME)
        db!!.execSQL("DROP TABLE IF EXISTS " + WORDS_TABLE_NAME)

        onCreate(db)
    }

    fun insert_user(uname: String, password: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(USER_KEY_UNAME, uname)
        contentValues.put(USER_KEY_PSW, password)

        var success = db.insert(USER_TABLE_NAME, null, contentValues)
    }

    fun insert_word(front: String, back: String, name_tag: String?) {
        val db = this.writableDatabase
        val contentValues: ContentValues = ContentValues()

        contentValues.put(WORDS_KEY_FRONT, front)
        contentValues.put(WORDS_KEY_BACK, back)
        contentValues.put(WORDS_KEY_TAG, name_tag)

        var success = db.insert(WORDS_TABLE_NAME, null, contentValues)
    }

    fun delete_word(where: String) {
        val db = this.writableDatabase
        db.delete(WORDS_TABLE_NAME, WORDS_KEY_FRONT + "=?", arrayOf(where))
    }

    fun delete_multiple_words(where: String) {
        val db = this.writableDatabase
        db.delete(WORDS_TABLE_NAME, WORDS_KEY_FRONT + "=?", arrayOf(where))
    }

    fun checkUser(uname: String): Boolean {
        val query = "SELECT * FROM $USER_TABLE_NAME WHERE $USER_KEY_UNAME = ?"
        var cursor: Cursor? = null
        val db = this.readableDatabase
        cursor = db.rawQuery(query, arrayOf(uname))

        if(cursor.count > 0)
            return true
        else
            return false
    }

    fun get_user_entries() : ArrayList<String>{
        var username_list: ArrayList<String> = ArrayList<String>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $USER_TABLE_NAME"
        var cursor: Cursor? = null

        cursor = db.rawQuery(query, null)

        if(cursor.moveToFirst()) {
            do {
                var id = cursor.getInt(cursor.getColumnIndex(USER_KEY_ID))
                var username = cursor.getString(cursor.getColumnIndex(USER_KEY_UNAME))
                username_list.add(username)
            } while(cursor.moveToNext())
            cursor.close()
            username_list.clear()
        }
        return username_list
    }
    fun get_words_by_tag(name_tag: String?) : ArrayList<DataSource> {
        var word_list: ArrayList<DataSource> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $WORDS_TABLE_NAME WHERE tag=?"
        var cursor: Cursor? = null

        cursor = db.rawQuery(query, arrayOf(name_tag))

        if(cursor.moveToFirst()) {
            do {
                var id = cursor.getInt(cursor.getColumnIndex(WORDS_KEY_ID))
                var front = cursor.getString(cursor.getColumnIndex(WORDS_KEY_FRONT))
                var back = cursor.getString(cursor.getColumnIndex(WORDS_KEY_BACK))
                var tag = cursor.getString(cursor.getColumnIndex(WORDS_KEY_TAG))
                var dt= DataSource(id, front, back, tag)
                word_list.add(dt)
            } while(cursor.moveToNext())
            cursor.close()
        }
        return word_list
    }

    fun validId(list: DataSource) {

    }
}