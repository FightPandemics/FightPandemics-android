package com.fightpandemics.home.ui.tabs.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.home.R

class HomeRequestFragment : Fragment() {

    companion object {
        fun newInstance() = HomeRequestFragment()
    }

    private lateinit var viewModel: HomeRequestViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_request_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeRequestViewModel::class.java)
        // TODO: Use the ViewModel
    }
}
