package com.fightpandemics.search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.search.dagger.inject
import com.fightpandemics.search.databinding.SearchFragmentBinding
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var searchFragmentBinding: SearchFragmentBinding? = null
    private lateinit var viewModel: SearchViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SearchFragmentBinding.inflate(inflater)
        searchFragmentBinding = binding

        createPost()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    override fun onDestroyView() {
        searchFragmentBinding = null
        super.onDestroyView()
    }

    private fun createPost() {
        /*(activity as MainActivity).findViewById<MaterialButton>(com.fightpandemics.R.id.fabCreateAsOrg)
            .setOnClickListener {
                findNavController().navigate(com.fightpandemics.R.id.action_searchFragment_to_createPostFragment)
            }

        (activity as MainActivity).findViewById<MaterialButton>(com.fightpandemics.R.id.fabCreateAsIndiv)
            .setOnClickListener {
                findNavController().navigate(com.fightpandemics.R.id.action_searchFragment_to_createPostFragment)
            }*/
    }
}
