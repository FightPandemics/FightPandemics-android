package com.fightpandemics.search.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.filter.ui.FilterAdapter
import com.fightpandemics.search.R
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter

data class CustomSuggestion(val suggestedQuery: String)

class SearchSuggestionsAdapter(inflater: LayoutInflater?) : SuggestionsAdapter<CustomSuggestion, SearchSuggestionsAdapter.SuggestionHolder>(
    inflater
) {


    class SuggestionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var recentSuggestion: TextView = itemView.findViewById((R.id.recent_search_text))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_previous_search, parent, false)
        return SuggestionHolder(v)
    }

    override fun onBindSuggestionHolder(
        suggestion: CustomSuggestion?,
        holder: SuggestionHolder?,
        position: Int
    ) {
        holder?.apply {
            recentSuggestion.setText(suggestion?.suggestedQuery)
        }
    }

    override fun getSingleViewHeight(): Int {
        // todo make this a constant somewhere for both the view holder and xml
        return 45
    }


}