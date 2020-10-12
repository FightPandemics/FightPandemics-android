package com.fightpandemics.home.ui.tabs.all

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.home.R
import com.fightpandemics.home.dagger.inject
import com.fightpandemics.home.ui.HomeViewModel
import com.fightpandemics.home.ui.tabs.PostsAdapter
import com.fightpandemics.utils.ViewModelFactory
import kotlinx.android.synthetic.main.home_all_fragment.*
import timber.log.Timber
import javax.inject.Inject

class HomeAllFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    // Obtain the ViewModel - use the ParentFragment as the Lifecycle owner
    private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() }) { viewModelFactory }

    private lateinit var progressBar: ProgressBar
    private lateinit var postList: RecyclerView
    private lateinit var postsAdapter: PostsAdapter

    companion object {
        fun newInstance() = HomeAllFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.home_all_fragment, container, false)
        progressBar = rootView.findViewById(R.id.progressBar)
        postList = rootView.findViewById(R.id.postList)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPosts()
    }

    private fun getPosts() {
        //errorLoadingText.visibility = View.GONE

        homeViewModel.getPosts(null)

        homeViewModel.postsState.observe(viewLifecycleOwner, {
            when {
                it.isLoading -> bindLoading(it.isLoading)
                it.posts!!.isNotEmpty() -> {
                    bindLoading(it.isLoading)
                    postList.visibility = View.VISIBLE
                    postsAdapter = PostsAdapter(it.posts)
                    postList.adapter = postsAdapter
                }
            }
        })


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
