package com.fightpandemics.search.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.filter.ui.FilterAdapter
import com.fightpandemics.search.R
import com.fightpandemics.search.utils.createDummyPost
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup

class SearchedPostsAdapter : RecyclerView.Adapter<SearchedPostsAdapter.SearchedPostHolder>() {
//    var searchedPostsDataFull = listOf<String>()
    var searchedPostsData = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onItemClickListener: ((Post) -> Unit)? = null

    class SearchedPostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(post: String, onItemClickListener: ((Post) -> Unit)?){
            val timePosted: TextView = itemView.findViewById((R.id.time_posted))
            val tags: ChipGroup = itemView.findViewById((R.id.tags_chip_group))
            val title: TextView = itemView.findViewById((R.id.post_title))
            title.text = post
            val content: TextView = itemView.findViewById((R.id.post_content))
            val viewMore: MaterialButton = itemView.findViewById((R.id.view_more_btn))
            val card: CardView = itemView.findViewById((R.id.search_post_card_layout))
            card.setOnClickListener { onItemClickListener?.invoke(createDummyPost(post))}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedPostHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_searched_post, parent, false)
        return SearchedPostHolder(v)
    }

    override fun onBindViewHolder(holder: SearchedPostHolder, position: Int) {
        // todo change to Post instead of String
        val post: String = searchedPostsData[position]
        holder.bind(post, onItemClickListener)
//        holder.title.setText(searchedPostsData[position])
    }

    override fun getItemCount(): Int {
        return searchedPostsData.size
    }
}