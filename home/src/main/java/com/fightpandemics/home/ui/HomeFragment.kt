package com.fightpandemics.home.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.fightpandemics.home.R
import com.fightpandemics.home.dagger.inject
import com.fightpandemics.home.ui.tabs.HomePagerAdapter
import com.fightpandemics.home.ui.tabs.OnTabSelected
import com.fightpandemics.home.utils.applyStyle
import com.fightpandemics.utils.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class HomeFragment : Fragment() {

    private lateinit var homePager: ViewPager
    private lateinit var homeTabs: TabLayout

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val rootView = inflater.inflate(R.layout.home_fragment, container, false)

        homePager = rootView.findViewById(R.id.homePager)
        homeTabs = rootView.findViewById(R.id.homeTabs)

        val toolbar: Toolbar = rootView.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        setupUi()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter -> Toast.makeText(context, "Filter Clicked", Toast.LENGTH_LONG).show()
        }
        return true
    }

    private fun setupUi() {
        val homePagerAdapter = HomePagerAdapter(requireActivity(), childFragmentManager)

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
