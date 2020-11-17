package com.fightpandemics.home.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.home.R
import com.fightpandemics.home.dagger.inject
import com.fightpandemics.home.databinding.EditDeleteBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class HomeOptionsBottomSheetFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    //@Inject lateinit var applicationContext: Context
    private var editDeleteBinding: EditDeleteBinding? = null
    private val homeViewModel: HomeViewModel
            by viewModels({ requireParentFragment() }) { viewModelFactory }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
            // Launch CreatePost filled with elements from this post.
            findNavController().navigate(
                HomeOptionsBottomSheetFragmentDirections
                    .actionHomeOptionsBottomSheetFragmentToCreatePostFragment(post)
            )
            dismissAllowingStateLoss()
        }

        // Delete a post.
        editDeleteBinding!!.btnDeletePost.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext(), R.style.PostMaterialDialog)
                .setTitle(R.string.delete_confirm_alert_title)
                .setMessage(R.string.delete_confirm_alert_msg)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete") { dialog, which ->
                    homeViewModel.onDeleteClicked(post!!)

                    // TODO - This Toast in the return of the delete. Replace with Snackbar
                    val context = requireContext().applicationContext
                    /*val layoutInflater = LayoutInflater.from(context?.applicationContext)
                    val customLayout = DeletePostFeedbackBinding.inflate(layoutInflater, delete_post_feedback, false) as View
                    //val customLayout = layoutInflater.inflate(R.layout.delete_post_feedback, delete_post_feedback)
                    //val customLayout = layoutInflater.inflate(R.layout.delete_post_feedback, findViewById(R.id.delete_post_feedback))
                    with(Toast(context?.applicationContext)) {
                        duration = Toast.LENGTH_SHORT
                        setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        view = customLayout
                        show()
                    }*/
                }
                .show()
            dismissAllowingStateLoss()
        }
    }
}