package com.fightpandemics.createpost.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.createpost.R
import com.fightpandemics.createpost.dagger.inject
import com.fightpandemics.createpost.databinding.FragmentCreatePostBinding
import javax.inject.Inject

class CreatePostFragment : Fragment() {

    @Inject
    lateinit var createPostViewModelFactory: ViewModelFactory
    private val createPostViewModel: CreatePostViewModel by viewModels { createPostViewModelFactory }

    private var fragmentCreatePostBinding: FragmentCreatePostBinding? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCreatePostBinding
            .inflate(inflater, container, false)
        fragmentCreatePostBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBar()
        setupViews()
        observeDurationBottomDialog()
    }

    override fun onDestroyView() {
        fragmentCreatePostBinding = null
        super.onDestroyView()
    }

    private fun setupToolBar() {
        fragmentCreatePostBinding!!.createPostToolbar.createPostToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupViews() {
        fragmentCreatePostBinding!!.name.setOnClickListener { displayOrganizationBottomDialog() }
        fragmentCreatePostBinding!!.offer.setOnClickListener { }
        fragmentCreatePostBinding!!.request.setOnClickListener { }
        fragmentCreatePostBinding!!.people.setOnClickListener { displayVisibilityBottomDialog() }
        fragmentCreatePostBinding!!.month.setOnClickListener { findNavController().navigate(R.id.action_createPostFragment_to_selectDurationFragment) }
        fragmentCreatePostBinding!!.tag.setOnClickListener { displayTagBottomDialog() }
        fragmentCreatePostBinding!!.post.setOnClickListener { }
    }

    private fun displayOrganizationBottomDialog() {
        val fragment = SelectOrganizationFragment()
        fragment.show(requireActivity().supportFragmentManager, fragment.tag)
    }

    private fun displayVisibilityBottomDialog() {
        val fragment = SelectVisibilityFragment()
        fragment.show(requireActivity().supportFragmentManager, fragment.tag)
    }

    private fun displayTagBottomDialog() {
        val fragment = SelectTagFragment()
        fragment.show(requireActivity().supportFragmentManager, fragment.tag)
    }

    private fun observeDurationBottomDialog() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.createPostFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains("duration")) {
                when (navBackStackEntry.savedStateHandle.get<String>("duration")) {
                    "A day" -> {
                        fragmentCreatePostBinding!!.month.text = getString(R.string.for_1_day)
                    }
                    "A week" -> {
                        fragmentCreatePostBinding!!.month.text = getString(R.string.for_1_week)
                    }
                    "A month" -> {
                        fragmentCreatePostBinding!!.month.text = getString(R.string.for_1_month)
                    }
                    else -> {
                        fragmentCreatePostBinding!!.month.text = getString(R.string.forever)
                    }
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    private fun postContent() {}
}