package com.fightpandemics.search.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.search.R
import com.fightpandemics.search.dagger.inject
import com.fightpandemics.search.databinding.InputSearchFragmentBinding
import com.fightpandemics.search.databinding.SearchFragmentBinding
import com.mancj.materialsearchbar.MaterialSearchBar
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InputSearchFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputSearchFragment2 : Fragment(), MaterialSearchBar.OnSearchActionListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: InputSearchFragmentBinding

    private lateinit var searchBar: MaterialSearchBar
    private lateinit var lastSearches: List<String>
    private lateinit var adapter: SearchedPostsAdapter

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        inject(this)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = InputSearchFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup search bar
//        setupSearchBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        // todo implement save queries to disk
//        saveSearchSuggestionToDisk(searchBar.lastSuggestions)
    }

    // do filter of post data & update recycler view data
    override fun onSearchConfirmed(text: CharSequence?) {
//        viewModel.filterPosts(text.toString())
        // todo send query back to past fragment
    }
    override fun onSearchStateChanged(enabled: Boolean) {}
    override fun onButtonClicked(buttonCode: Int) {}

    private fun setupSearchBar(){
        // todo add more stuff
        searchBar = binding.searchBar
        searchBar.setSpeechMode(false)
        searchBar.openSearch()
        searchBar.setHint("Search")
        searchBar.setOnSearchActionListener(this)

        // setup custom suggestions for searchbar (initialize adapter)
//        setupCustomSuggestions()

    }

    private fun setupCustomSuggestions(){
        val inflater = requireActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customSuggestionAdapter = SearchSuggestionsAdapter(inflater = inflater)
        val suggestions = mutableListOf<CustomSuggestion>()
        for (i in 0..11){
            suggestions.add(CustomSuggestion("potatos"))
        }
        customSuggestionAdapter.suggestions = suggestions
        searchBar.setCustomSuggestionAdapter(customSuggestionAdapter)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InputSearchFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InputSearchFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}