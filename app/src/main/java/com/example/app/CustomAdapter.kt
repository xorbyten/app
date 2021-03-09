package com.example.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.app.words_db.DataSource

class CustomAdapter():
RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    val data: ArrayList<DataSource> = ArrayList()
    var tracker: SelectionTracker<Long>? = null
    var listener: myLongClickListener? = null

    // Constructor instead
    fun setData(newData: ArrayList<DataSource>, longClickListener: myLongClickListener) {
        data.clear()
        data.addAll(newData)

        setHasStableIds(true)

        this.listener = longClickListener

        notifyDataSetChanged()
    }

    interface myLongClickListener {
        fun myLongClick(dataSource: DataSource, position: Int) : Boolean
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val tv_front: TextView = view.findViewById(R.id.tv_front) as TextView
        val tv_back: TextView = view.findViewById(R.id.tv_back) as TextView

        fun bind(item: DataSource, isActivated: Boolean = false, action: myLongClickListener?) {
            tv_front.text = item._front
            tv_back.text = item._back

            itemView.isActivated = isActivated

            view.setOnLongClickListener {
                action!!.myLongClick(item, adapterPosition)
            }
        }

        fun getItemDetails() : ItemDetailsLookup.ItemDetails<Long> =
                object: ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition(): Int = adapterPosition
            override fun getSelectionKey(): Long? = itemId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data[position]

        holder.bind(currentItem, tracker!!.isSelected(position.toLong()), listener)
    }

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long = position.toLong()
}