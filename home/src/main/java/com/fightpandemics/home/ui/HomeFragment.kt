package com.fightpandemics.home.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.fightpandemics.home.R
import com.fightpandemics.home.dagger.inject
import com.fightpandemics.home.ui.tabs.HomePagerAdapter
import com.fightpandemics.home.ui.tabs.OnTabSelected
import com.fightpandemics.home.utils.applyStyle
import com.fightpandemics.utils.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.item_tab_appbar.*
import javax.inject.Inject

class HomeFragment : Fragment() {

    private lateinit var homePager : ViewPager
    private lateinit var homeTabs : TabLayout

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: HomeViewModel

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.home_fragment, container, false)

        homePager = rootView.findViewById(R.id.homePager)
        homeTabs = rootView.findViewById(R.id.homeTabs)

        setupUi()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private fun setupUi() {
        val homePagerAdapter =
            HomePagerAdapter(
                requireActivity(),
                childFragmentManager
            )

        homePager.adapter = homePagerAdapter
        homeTabs.setupWithViewPager(homePager)
        homeTabs.addOnTabSelectedListener(OnTabSelected())

        for (i in 0 until homeTabs.tabCount) {
            val title = homeTabs.getTabAt(i)?.text

            homeTabs.getTabAt(i)?.setCustomView(R.layout.item_tab_title)
            val tabTitle = homeTabs.getTabAt(i)?.customView?.findViewById<TextView>(R.id.tv_title)
            tabTitle?.text = title
            tabTitle?.applyStyle(tabTitle.isSelected)
        }
    }

}