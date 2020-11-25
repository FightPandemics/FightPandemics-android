package com.fightpandemics.home.ui.tabs.all

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.filter.ui.FilterRequest
import com.fightpandemics.home.dagger.inject
import com.fightpandemics.home.databinding.HomeAllFragmentBinding
import com.fightpandemics.home.ui.HomeViewModel
import com.fightpandemics.home.ui.tabs.PostsAdapter
import timber.log.Timber
import javax.inject.Inject

/*
* created by Osaigbovo Odiase
* */
class HomeAllFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    // Obtain the ViewModel - use the ParentFragment as the Lifecycle owner
    private val homeViewModel: HomeViewModel
            by viewModels({ requireParentFragment() }) { viewModelFactory }

    private var homeAllFragmentBinding: HomeAllFragmentBinding? = null
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
    ): View {
        // Inflate the layout for this fragment
        val binding = HomeAllFragmentBinding
            .inflate(inflater, container, false)
        homeAllFragmentBinding = binding
        homeAllFragmentBinding!!.postList.itemAnimator = null
        postsAdapter = PostsAdapter(homeViewModel)
        postsAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<FilterRequest>("filters")
            ?.observe(
                viewLifecycleOwner
            ) { result ->
                // TODO: remove timber Do something with the result.
                Timber.e("My filters are: ${result.location}, ${result.fromWhomFilters.toString()}, ${result.typeFilters.toString()}")
            }
        // If you’d only like to handle a result only once, you must call remove() on the
        // SavedStateHandle to clear the result. If you do not remove the result, the LiveData
        // will continue to return the last result to any new Observer instances.
        // findNavController().currentBackStackEntry?.savedStateHandle?.remove<List<String>>("key")
        super.onViewCreated(view, savedInstanceState)
        getPosts()
    }

    override fun onDestroyView() {
        homeAllFragmentBinding = null
        super.onDestroyView()
    }

    private fun getPosts() {
        //errorLoadingText.visibility = View.GONE

        homeViewModel.postsState.observe(viewLifecycleOwner, {
            when {
                it.isLoading -> bindLoading(it.isLoading)
                it.posts!!.isNotEmpty() -> {
                    bindLoading(it.isLoading)
                    homeAllFragmentBinding!!.postList.visibility = View.VISIBLE
                    postsAdapter.submitList(it.posts)
                    postsAdapter.onItemClickListener = { post ->
                        Timber.e("${post.author?.name}")
                        //findNavController().navigate(PokeListFragmentDirections.actionPokeListFragmentToPokeDetailFragment(post))
                    }
                }
                it.error != null -> {
                    bindLoading(it.isLoading)
                    homeAllFragmentBinding!!.postList.visibility = View.GONE
                    //bindError()
                }
            }
        })
        homeAllFragmentBinding!!.postList.adapter = postsAdapter
    }

    private fun bindLoading(isLoading: Boolean) {
        homeAllFragmentBinding!!.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
