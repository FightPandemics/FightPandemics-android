package com.fightpandemics.filter.ui

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fightpandemics.home.R
import kotlinx.android.synthetic.main.filter_location_item.view.*

class FilterAdapter : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

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

    }

    override fun getItemCount(): Int {
        return 2
        return data.size
    }

}

