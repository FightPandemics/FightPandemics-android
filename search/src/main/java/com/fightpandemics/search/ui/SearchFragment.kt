package com.fightpandemics.search.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.fightpandemics.search.R
import com.fightpandemics.search.dagger.inject
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.search.databinding.SearchFragmentBinding
import com.fightpandemics.search.utils.TAB_TITLES
import com.fightpandemics.ui.MainActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mancj.materialsearchbar.MaterialSearchBar
import timber.log.Timber
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: SearchFragmentBinding

    private lateinit var searchTabs: TabLayout
    private lateinit var searchPager: ViewPager2

    private lateinit var searchBar: MaterialSearchBar
    private lateinit var lastSearches: List<String>
    private lateinit var adapter: SearchedPostsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        createPost()
        binding = SearchFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup menu
        setHasOptionsMenu(true)

        // setup toolbar
        val toolbar = binding.searchToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        // tab layout and view pager
        setupTabs()

        // setup search bar
        setupSearchBar()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        // todo implement save queries to disk
//        saveSearchSuggestionToDisk(searchBar.lastSuggestions)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter -> {
                Toast.makeText(requireContext(), "Filter", Toast.LENGTH_SHORT).show()
                // todo add action to filter module
//                findNavController()
//                    .navigate(com.fightpandemics.R.id.action_homeFragment_to_filterFragment)
                findNavController().navigate(com.fightpandemics.R.id.action_searchFragment_to_inputSearchFragment)
            }
        }
        return true
    }

    private fun setupTabs(){
        searchTabs = binding.searchTabs
        searchPager = binding.searchPager
        searchPager.adapter = SearchPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        // Connect tabs to viewpager
        TabLayoutMediator(searchTabs, searchPager) { tab, position ->
            tab.text = this.resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setupSearchBar(){
        // todo change the top searchBar to be a edittext view
        searchBar = binding.searchBar
        searchBar.setSpeechMode(false)
        searchBar.openSearch()
        searchBar.setHint("Search")
        searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener{
            override fun onSearchStateChanged(enabled: Boolean) {}
            override fun onButtonClicked(buttonCode: Int) {}
            override fun onSearchConfirmed(text: CharSequence?) {
                viewModel.filterPosts(text.toString())
            }
        })
        // setup custom suggestions for searchbar (initialize adapter)
//        setupCustomSuggestions()

//        searchBar.setOnClickListener {
//            findNavController().navigate(com.fightpandemics.R.id.action_searchFragment_to_inputSearchFragment)
//        }
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

    private fun createPost() {
        (activity as MainActivity).findViewById<MaterialButton>(com.fightpandemics.R.id.fabCreateAsOrg)
            .setOnClickListener {
                findNavController().navigate(com.fightpandemics.R.id.action_searchFragment_to_createPostFragment)
            }

        (activity as MainActivity).findViewById<MaterialButton>(com.fightpandemics.R.id.fabCreateAsIndiv)
            .setOnClickListener {
                findNavController().navigate(com.fightpandemics.R.id.action_searchFragment_to_createPostFragment)
            }
    }
}
