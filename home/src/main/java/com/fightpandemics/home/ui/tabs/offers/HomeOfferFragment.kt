package com.fightpandemics.home.ui.tabs.offers

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.home.R

class HomeOfferFragment : Fragment() {

    companion object {
        fun newInstance() = HomeOfferFragment()
    }

    private lateinit var viewModel: HomeOfferViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_offer_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeOfferViewModel::class.java)
        // TODO: Use the ViewModel
    }

}