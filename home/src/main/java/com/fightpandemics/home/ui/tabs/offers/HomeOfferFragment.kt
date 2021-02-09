package com.fightpandemics.home.ui.tabs.offers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.home.R
import com.fightpandemics.home.dagger.inject
import com.fightpandemics.home.ui.HomeViewModel
import com.fightpandemics.home.ui.tabs.PostsAdapter
import javax.inject.Inject

class HomeOfferFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val homeViewModel: HomeViewModel by activityViewModels { viewModelFactory }

    private lateinit var progressBar: ProgressBar
    private lateinit var postList: RecyclerView
    private lateinit var postAdapter: PostsAdapter

    companion object {
        fun newInstance() = HomeOfferFragment()
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
        val rootView = inflater.inflate(R.layout.home_offer_fragment, container, false)
        progressBar = rootView.findViewById(R.id.progressBar)
        postList = rootView.findViewById(R.id.postList)
        postAdapter = PostsAdapter(homeViewModel)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOfferPosts()
    }

    private fun getOfferPosts() {
        // errorLoadingText.visibility = View.GONE

        // homeViewModel.getOffers("offer")
        homeViewModel.offerState.observe(
            viewLifecycleOwner,
            {
                when {
                    it.isLoading -> bindLoading(it.isLoading)
                    it.posts!!.isNotEmpty() -> {
                        bindLoading(it.isLoading)
                        postList.visibility = View.VISIBLE
                        postAdapter.submitList(it.posts)
                        postAdapter.notifyDataSetChanged()
                    }
                }
            }
        )

        postList.adapter = postAdapter

    }

    private fun bindLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
