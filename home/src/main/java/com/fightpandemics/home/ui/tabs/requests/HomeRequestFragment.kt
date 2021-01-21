package com.fightpandemics.home.ui.tabs.requests

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.home.R
import com.fightpandemics.home.dagger.inject
import com.fightpandemics.home.ui.HomeViewModel
import com.fightpandemics.home.ui.tabs.PostsAdapter
import javax.inject.Inject

class HomeRequestFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    // Obtain the ViewModel - use the ParentFragment as the Lifecycle owner
    private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() }) { viewModelFactory }

    private lateinit var progressBar: ProgressBar
    private lateinit var requestList: RecyclerView
    private lateinit var homeRequestAdapter: PostsAdapter

    companion object {
        fun newInstance() = HomeRequestFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.home_request_fragment, container, false)
        progressBar = rootView.findViewById(R.id.progressBar)
        requestList = rootView.findViewById(R.id.requestList)
        homeRequestAdapter = PostsAdapter(homeViewModel)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRequestPosts()
    }

    private fun getRequestPosts() {
        // errorLoadingText.visibility = View.GONE

        // homeViewModel.getPosts("request")
        homeViewModel.requestState.observe(
            viewLifecycleOwner,
            {
                when {
                    it.isLoading -> bindLoading(it.isLoading)
                    it.posts!!.isNotEmpty() -> {
                        bindLoading(it.isLoading)
                        requestList.visibility = View.VISIBLE
                        homeRequestAdapter.submitList(it.posts)
                    }
                }
            }
        )
        homeRequestAdapter.notifyDataSetChanged()
        requestList.adapter = homeRequestAdapter

//        sharedHomeViewModel.state.observe(viewLifecycleOwner, onChanged = {
//            when {
//                it.isLoading -> {
//                    bindLoading(it.isLoading)
//                }
//                it.pokemons!!.isNotEmpty() -> {
//                    progressBar.visibility = View.GONE
//                    errorLoadingText.visibility = View.GONE
//                    pokeList.visibility = View.VISIBLE
//                    pokeAdapter = PokeAdapter(it.pokemons)
//                    pokeList.adapter = pokeAdapter
//
//                    pokeAdapter.onItemClickListener = { pokemon ->
//                        findNavController()
//                            .navigate(PokeListFragmentDirections
//                                .actionPokeListFragmentToPokeDetailFragment(pokemon.id))
//                    }
//
//                    pokeAdapter.registerAdapterDataObserver(object :
//                        RecyclerView.AdapterDataObserver() {
//                        override fun onChanged() {
//                            super.onChanged()
//                            if (pokeAdapter.getItemCount() <= 0) {
//                                noResult.setVisibility(View.VISIBLE)
//                                noResult.text = getString(R.string.no_result, searchView.query)
//                            } else noResult.setVisibility(View.GONE)
//                        }
//                    })
//
//                    sharedHomeViewModel.pokemonsCache(it.pokemons)
//                }
//                it.error != null -> {
//                    progressBar.visibility = View.GONE
//                    pokeList.visibility = View.GONE
//                    bindError()
//                }
//            }
//        })
    }

    private fun bindLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
