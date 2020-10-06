package com.fightpandemics.home.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.fightpandemics.home.R
import com.fightpandemics.home.dagger.inject
import com.fightpandemics.home.ui.tabs.HomePagerAdapter
import com.fightpandemics.home.utils.TAB_TITLES
import com.fightpandemics.utils.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialSharedAxis
import javax.inject.Inject

class HomeFragment : Fragment() {

    private lateinit var homePager: ViewPager2
    private lateinit var homeTabs: TabLayout

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: HomeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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
            R.id.filter -> {
                exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
                    duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                }
                reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
                    duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
                }
                findNavController().navigate(com.fightpandemics.R.id.action_homeFragment_to_filterFragment)
            }
        }
        return true
    }

    private fun setupUi() {
        val homePagerAdapter =
            HomePagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

        homePager.adapter = homePagerAdapter

        TabLayoutMediator(homeTabs, homePager) { tab, position ->
            tab.text = this.resources.getString(TAB_TITLES[position])
            //homeTabs.addOnTabSelectedListener(OnTabSelected())
        }.attach()
    }
}
