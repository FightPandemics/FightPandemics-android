package com.fightpandemics.home.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.fightpandemics.core.result.EventObserver
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.home.R
import com.fightpandemics.home.R.layout
import com.fightpandemics.home.dagger.inject
import com.fightpandemics.home.ui.tabs.HomePagerAdapter
import com.fightpandemics.home.utils.TAB_TITLES
import com.fightpandemics.ui.MainActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/*
* created by Osaigbovo Odiase
* */
@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var homePager: ViewPager2
    private lateinit var homeTabs: TabLayout

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }

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
        val rootView = inflater.inflate(layout.home_fragment, container, false)

        homePager = rootView.findViewById(R.id.homePager)
        homeTabs = rootView.findViewById(R.id.homeTabs)

        val toolbar: Toolbar = rootView.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)

        setupUi()
        createPost()

        val fab: FloatingActionButton = activity?.findViewById(com.fightpandemics.R.id.fab)!!

        homeViewModel.isDeleted.observe(requireActivity(), EventObserver {

            if (it.isNotBlank()) {
                Timber.e(it)

                Snackbar.make(fab, "Snackbar over BottomAppBar", Snackbar.LENGTH_SHORT)
                    .apply { anchorView = fab }.show()
            }
        })

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter -> {
                findNavController()
                    .navigate(com.fightpandemics.R.id.action_homeFragment_to_filterFragment)
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

    private fun createPost() {
        (activity as MainActivity).findViewById<MaterialButton>(com.fightpandemics.R.id.fabCreateAsOrg)
            .setOnClickListener {
                findNavController().navigate(com.fightpandemics.R.id.action_homeFragment_to_createPostFragment)
            }

        (activity as MainActivity).findViewById<MaterialButton>(com.fightpandemics.R.id.fabCreateAsIndiv)
            .setOnClickListener {
                findNavController().navigate(com.fightpandemics.R.id.action_homeFragment_to_createPostFragment)
            }
    }
}
