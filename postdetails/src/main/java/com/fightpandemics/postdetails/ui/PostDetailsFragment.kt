package com.fightpandemics.postdetails.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.postdetails.R

class PostDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = PostDetailsFragment()
    }

    private lateinit var viewModel: PostDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.post_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PostDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }
}