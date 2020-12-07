package com.fightpandemics.createpost.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_create_post.*
import javax.inject.Inject

class CreatePostFragment : Fragment() {

    @Inject
    lateinit var createPostViewModelFactory: ViewModelFactory
    private val createPostViewModel: CreatePostViewModel by viewModels { createPostViewModelFactory }

    private var fragmentCreatePostBinding: FragmentCreatePostBinding? = null
    private lateinit var titleTextWatcher: TextWatcher
    private val chipTexts: ArrayList<String> = ArrayList()

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
        setTitleTextWatcher()
        setupViews()
        observeDurationBottomDialog()
        observeVisibilityBottomDialog()
        observeTagBottomDialog()
        displayChosenTags()
    }

    override fun onDestroyView() {
        fragmentCreatePostBinding = null
        super.onDestroyView()
    }

    private fun setupToolBar() {
        fragmentCreatePostBinding!!.createPostToolbar.createPostToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupViews() {
        fragmentCreatePostBinding!!.toggleBt1.isChecked = true
        fragmentCreatePostBinding!!.etTitle.addTextChangedListener(titleTextWatcher)
        fragmentCreatePostBinding!!.name.setOnClickListener {
            displayOrganizationBottomDialog()
        }
        fragmentCreatePostBinding!!.people.setOnClickListener {
            findNavController().navigate(R.id.action_createPostFragment_to_selectVisibilityFragment)
        }
        fragmentCreatePostBinding!!.month.setOnClickListener {
            findNavController().navigate(R.id.action_createPostFragment_to_selectDurationFragment)
        }
        fragmentCreatePostBinding!!.tag.setOnClickListener {
            findNavController().navigate(R.id.action_createPostFragment_to_selectTagFragment)
        }
        fragmentCreatePostBinding!!.post.setOnClickListener { }
    }

    private fun setTitleTextWatcher() {
        titleTextWatcher = object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count >= 60) {
                    fragmentCreatePostBinding!!.error.visibility = View.VISIBLE
                } else {
                    fragmentCreatePostBinding!!.error.visibility = View.GONE
                }
            }
        }
    }

    private fun displayOrganizationBottomDialog() {
        val fragment = SelectOrganizationFragment()
        fragment.show(requireActivity().supportFragmentManager, fragment.tag)
    }

    private fun observeDurationBottomDialog() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.createPostFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains("duration")) {
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
            if (event == Lifecycle.Event.ON_PAUSE) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    private fun observeVisibilityBottomDialog() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.createPostFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains("visibility")) {
                when (navBackStackEntry.savedStateHandle.get<String>("visibility")) {
                    "My neighbourhood" -> {
                        fragmentCreatePostBinding!!.people.text = getString(R.string.people_in_my_neighbourhood)
                    }
                    "My city" -> {
                        fragmentCreatePostBinding!!.people.text = getString(R.string.people_in_my_city)
                    }
                    "My country" -> {
                        fragmentCreatePostBinding!!.people.text = getString(R.string.people_in_my_country)
                    }
                    else -> {
                        fragmentCreatePostBinding!!.people.text = getString(R.string.anyone)
                    }
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    private fun observeTagBottomDialog() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.createPostFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains("tag1")) {
                chipTexts.add(navBackStackEntry.savedStateHandle.get<String>("tag1")!!)
                chipTexts.add(navBackStackEntry.savedStateHandle.get<String>("tag2")!!)
                chipTexts.add(navBackStackEntry.savedStateHandle.get<String>("tag3")!!)
            }
            displayChosenTags()
        }
        navBackStackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    @SuppressLint("InflateParams")
    private fun displayChosenTags() {
        if (tag_chip_group.childCount > 0) {
            tag_chip_group.removeAllViews()
        }

        for (text in chipTexts) {
            val chip = layoutInflater.inflate(R.layout.item_create_post_tag, null, false) as Chip
            chip.text = text
            tag_chip_group.addView(chip)
        }
        chipTexts.clear()
    }

    private fun postContent() {}
}