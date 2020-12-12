package com.fightpandemics.search.ui.tabs.posts

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.search.R
import com.fightpandemics.search.databinding.SearchFragmentBinding
import com.fightpandemics.search.ui.SearchViewModel
import com.fightpandemics.search.ui.SearchedPostsAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchPostsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchPostsFragment : Fragment() {

    private lateinit var binding: SearchFragmentBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: SearchedPostsAdapter

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
//        binding = SearchPostsFragmentBinding.inflate(inflater)
//        return binding.root
        val root = inflater.inflate(R.layout.fragment_search_posts, container, false)

        // Get view model
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        // setup posts in recycler view
        displayPosts(root)

        return root
    }

    private fun displayPosts(root: View) {
        // recycler view
        adapter = SearchedPostsAdapter()
        val rv = root.findViewById<RecyclerView>(R.id.searched_posts_recycler_view)
        rv.adapter = adapter

        // todo load all posts here
        viewModel.loadAllPosts()

        // update recycler view adapter
        viewModel.filteredPosts.observe(viewLifecycleOwner, Observer { filteredList ->
            adapter.searchedPostsData = filteredList
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchPostsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchPostsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}