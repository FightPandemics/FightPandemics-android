package com.fightpandemics.search.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.filter.ui.FilterAdapter
import com.fightpandemics.search.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup

class SearchedPostsAdapter : RecyclerView.Adapter<SearchedPostsAdapter.SearchedPostHolder>(){
    var searchedPostsData = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class SearchedPostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timePosted: TextView = itemView.findViewById((R.id.time_posted))
        val tags: ChipGroup = itemView.findViewById((R.id.tags_chip_group))
        val title: TextView = itemView.findViewById((R.id.post_title))
        val content: TextView = itemView.findViewById((R.id.post_content))
        val viewMore: MaterialButton = itemView.findViewById((R.id.view_more_btn))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedPostHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_searched_post, parent, false)
        return SearchedPostHolder(v)
    }

    override fun onBindViewHolder(holder: SearchedPostHolder, position: Int) {
        holder.title.setText(searchedPostsData[position])
    }

    override fun getItemCount(): Int {
        return searchedPostsData.size
    }


}