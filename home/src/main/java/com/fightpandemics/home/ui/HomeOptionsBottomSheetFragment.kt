package com.fightpandemics.home.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.home.R
import com.fightpandemics.home.dagger.inject
import com.fightpandemics.home.databinding.EditDeleteBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

// BottomSheetDialog for Edit and Delete options.
class HomeOptionsBottomSheetFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var editDeleteBinding: EditDeleteBinding? = null

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = EditDeleteBinding.inflate(inflater, container, false)
        editDeleteBinding = binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val post = arguments?.let { HomeOptionsBottomSheetFragmentArgs.fromBundle(it).post }
        setUpActions(post)
    }

    override fun onDestroyView() {
        editDeleteBinding = null
        super.onDestroyView()
    }

    private fun setUpActions(post: Post?) {
        // Edit a post.
        editDeleteBinding!!.btnEditPost.setOnClickListener {
            // Launch CreatePost filled with elements from this "post".
            findNavController().navigate(
                HomeOptionsBottomSheetFragmentDirections
                    .actionHomeOptionsBottomSheetFragmentToCreatePostFragment(post)
            )
            dismissAllowingStateLoss()
        }

        // Delete a post.
        editDeleteBinding!!.btnDeletePost.setOnClickListener {
            // Launch DialogFragment the "post"
            val bundle = Bundle()
            bundle.putParcelable("post", post)
            DeleteDialogFragment.newInstance(bundle).show(childFragmentManager, "")
            dismissAllowingStateLoss()
        }
    }
}
