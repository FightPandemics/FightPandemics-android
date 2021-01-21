package com.fightpandemics.home.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.filter.ui.FilterRequest
import com.fightpandemics.home.R
import com.fightpandemics.home.dagger.inject
import com.fightpandemics.home.databinding.HomeFilterChipLayoutBinding
import com.fightpandemics.home.ui.tabs.HomePagerAdapter
import com.fightpandemics.home.utils.TAB_TITLES
import com.fightpandemics.ui.MainActivity
import com.fightpandemics.home.databinding.HomeFragmentBinding
import com.fightpandemics.home.databinding.SingleChipLayoutBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

/*
* created by Osaigbovo Odiase
* */
@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
    private var homeFragmentBinding: HomeFragmentBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setHasOptionsMenu(true)
        val binding = HomeFragmentBinding.inflate(inflater)
        homeFragmentBinding = binding

        (activity as AppCompatActivity).setSupportActionBar(homeFragmentBinding!!.appBar.toolbar)

        setHasOptionsMenu(true)

        setupUi()
        createPost()

        /*val fab: FloatingActionButton = activity?.findViewById(com.fightpandemics.R.id.fab)!!

        homeViewModel.isDeleted.observe(
            requireActivity(),
            EventObserver {

                if (it.isNotBlank()) {
                    Timber.e(it)

                    Snackbar.make(fab, "Snackbar over BottomAppBar", Snackbar.LENGTH_SHORT)
                        .apply { anchorView = fab }.show()
                }
            }
        )*/

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<FilterRequest>("filters")
            ?.observe(viewLifecycleOwner)
            { result ->
                Timber.e("My filters are: ${result.typeFilters}")


                // TODO - Use stateFLow to monitor the content from the filter
                homeViewModel.filterState.value = result.typeFilters as MutableList<String>


                homeFragmentBinding!!.appBar.horizontalScrollBar.visibility =
                    if (result.typeFilters!!.isNotEmpty()) View.VISIBLE else View.GONE

                homeFragmentBinding!!.appBar.filterChipGroup.removeAllViews()
                for (type: String? in result.typeFilters!!) {
                    val homeFilterChipLayoutBinding = HomeFilterChipLayoutBinding.inflate(
                        LayoutInflater.from(this.context),
                        homeFragmentBinding!!.appBar.filterChipGroup,
                        false
                    )
                    homeFilterChipLayoutBinding.chip.text = type

                    homeFilterChipLayoutBinding.chip.setOnCloseIconClickListener {
                        val f = result.typeFilters as MutableList<String?>
                        f.remove(type)


                        val anim = AlphaAnimation(1f, 0f)
                        anim.duration = 250
                        anim.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationRepeat(animation: Animation?) {}
                            override fun onAnimationEnd(animation: Animation?) {
                                homeFragmentBinding!!.appBar.filterChipGroup.removeView(
                                    homeFilterChipLayoutBinding.chip
                                )
                            }

                            override fun onAnimationStart(animation: Animation?) {}
                        })
                        it.startAnimation(anim)

                        homeFragmentBinding!!.homeFragment.removeView(homeFragmentBinding!!.appBar.filterChipGroup)

                        val layoutTransition = homeFragmentBinding!!.homeFragment.layoutTransition
                        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
                        homeFragmentBinding!!.appBar.horizontalScrollBar.visibility =
                            if (result.typeFilters!!.isNotEmpty()) {
                                View.VISIBLE
                            } else {
                                View.GONE
                            }
                    }

                    homeFragmentBinding!!.appBar
                        .filterChipGroup.addView(homeFilterChipLayoutBinding.chip)
                }
            }
        // If you’d only like to handle a result only once, you must call remove() on the
        // SavedStateHandle to clear the result. If you do not remove the result, the LiveData
        // will continue to return the last result to any new Observer instances.
        // findNavController().currentBackStackEntry?.savedStateHandle?.remove<List<String>>("key")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        homeFragmentBinding = null
        super.onDestroyView()
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

        homeFragmentBinding!!.homePager.adapter = homePagerAdapter

        TabLayoutMediator(
            homeFragmentBinding!!.appBar.homeTabs,
            homeFragmentBinding!!.homePager
        ) { tab, position ->
            tab.text = this.resources.getString(TAB_TITLES[position])
            // homeTabs.addOnTabSelectedListener(OnTabSelected())
        }.attach()
    }

    private fun createPost() {
        // TODO - remove fabCreateAsOrg and fabCreateAsIndiv
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
