package com.fightpandemics.filter.ui

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.home.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.filter_location_item.view.*

class FilterAdapter(val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    val MAX_RECYCLER_VIEW_SIZE = 3

    var data = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var address : TextView

        init {
            address = itemView.findViewById((R.id.location_item_text))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.filter_location_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.address.setText(data[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(data[position])
        }

    }

    override fun getItemCount(): Int {
        if (data.size > MAX_RECYCLER_VIEW_SIZE){
            return MAX_RECYCLER_VIEW_SIZE
        }
        return data.size
    }

    interface OnItemClickListener {
        fun onClick(locationSelected: String)
    }

}

