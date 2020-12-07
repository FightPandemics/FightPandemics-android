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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fightpandemics.search.R
import com.fightpandemics.search.dagger.inject
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.search.databinding.SearchFragmentBinding
import com.fightpandemics.ui.MainActivity
import com.google.android.material.button.MaterialButton
import com.mancj.materialsearchbar.MaterialSearchBar
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: SearchFragmentBinding

    private lateinit var searchBar: EditText
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

        // Get view model
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        // setup menu
        setHasOptionsMenu(true)

        // setup toolbar
        val toolbar = binding.searchToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        // setup search bar
        setupSearchBar()

        // setup posts in recycler view
        displayPosts()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter -> {
                Toast.makeText(requireContext(), "Filter", Toast.LENGTH_SHORT).show()
                // todo add action
//                findNavController()
//                    .navigate(com.fightpandemics.R.id.action_homeFragment_to_filterFragment)
            }
        }
        return true
    }

    private fun setupSearchBar(){
        // todo change the top searchBar to be a edittext view
        searchBar = binding.searchBar
        searchBar.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_searchFragment_to_inputSearchFragment)
        }
    }


    private fun displayPosts(){
        // todo put at top of class similar to filter module
        adapter = SearchedPostsAdapter()
        binding.searchedPostsRecyclerView.adapter = adapter

        // todo load all posts here
        viewModel.loadAllPosts()

        // update recycler view adapter
        viewModel.filteredPosts.observe(viewLifecycleOwner, Observer { filteredList ->
            adapter.searchedPostsData = filteredList
        })

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
