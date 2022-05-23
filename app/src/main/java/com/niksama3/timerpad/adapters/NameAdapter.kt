package com.niksama3.timerpad.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.niksama3.timerpad.R

class NameAdapter : RecyclerView.Adapter<NameAdapter.NameViewHolder>() {

    private val list = (1.. 100).toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.name_card, parent, false)
        return NameViewHolder(view)
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class NameViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        private val textView = viewItem.findViewById<TextView>(R.id.tv_name)

        fun bind(position: Int) {
            textView.text = "Full name $position"
        }
    }
}