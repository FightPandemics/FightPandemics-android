package com.fightpandemics.login.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.login.R

class LocationAdapter(val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<LocationAdapter.ViewHolder>() {
    val MAX_RECYCLER_VIEW_SIZE = 5

    var placesNames = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var address: TextView = itemView.findViewById((R.id.location_item_text))
        fun bind(location: String, clickListener: OnItemClickListener) {
            address.text = location
            itemView.setOnClickListener {
                clickListener.onAutocompleteLocationClick(location)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.filter_location_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = placesNames[position]
        holder.bind(item, onItemClickListener)
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
