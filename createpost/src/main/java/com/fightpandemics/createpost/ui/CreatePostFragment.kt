package com.fightpandemics.createpost.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
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
    }

    override fun onDestroyView() {
        fragmentCreatePostBinding = null
        super.onDestroyView()
    }

    private fun setupToolBar() {
        fragmentCreatePostBinding!!.createPostToolbar.createPostToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupViews() {
        fragmentCreatePostBinding!!.name.setOnClickListener { showToast(it) }
        fragmentCreatePostBinding!!.offer.setOnClickListener { showToast(it) }
        fragmentCreatePostBinding!!.request.setOnClickListener { showToast(it) }
        fragmentCreatePostBinding!!.people.setOnClickListener { showToast(it) }
        fragmentCreatePostBinding!!.month.setOnClickListener { showToast(it) }
        fragmentCreatePostBinding!!.tag.setOnClickListener { showToast(it) }
        fragmentCreatePostBinding!!.post.setOnClickListener { showToast(it) }
    }

    private fun showToast(view: View) {
        Toast.makeText(requireContext(), view.toString() + "was clicked", Toast.LENGTH_SHORT).show()
    }
}