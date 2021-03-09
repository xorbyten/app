package com.example.app.words_db

data class DataSource(var _id: Int, var _front: String, var _back: String, var _tag: String) {

    private var isSelected = false

    fun setSelection(select: Boolean) {
        isSelected = select
    }

    fun isSelected() : Boolean {
        return isSelected
    }
}