package com.fightpandemics.filter.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.home.R

class FilterAdapter(val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    val MAX_RECYCLER_VIEW_SIZE = 5

    var placesNames = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var address: TextView = itemView.findViewById((R.id.location_item_text))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.filter_location_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.address.setText(placesNames[position])
        holder.itemView.setOnClickListener {
            onItemClickListener.onAutocompleteLocationClick(placesNames[position])
        }
    }

    override fun getItemCount(): Int {
        if (placesNames.size > MAX_RECYCLER_VIEW_SIZE) {
            return MAX_RECYCLER_VIEW_SIZE
        }
        return placesNames.size
    }

    interface OnItemClickListener {
        fun onAutocompleteLocationClick(locationSelected: String)
    }
}
