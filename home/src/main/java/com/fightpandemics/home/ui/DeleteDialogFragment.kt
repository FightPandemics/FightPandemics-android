package com.fightpandemics.home.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.home.R
import com.fightpandemics.home.dagger.inject
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
* created by Osaigbovo Odiase
* */
// Dialog for Delete action
@ExperimentalCoroutinesApi
class DeleteDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var homeViewModel: HomeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun getDialog(): Dialog? {
        homeViewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(HomeViewModel::class.java)

        val context = requireActivity()

        return MaterialAlertDialogBuilder(requireContext(), R.style.PostMaterialDialog)
            .setTitle(R.string.delete_confirm_alert_title)
            .setMessage(R.string.delete_confirm_alert_msg)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete") { _, _ ->
                context.lifecycleScope.launch {
                    requireArguments()
                        .getParcelable<Post>("post")?.let { homeViewModel.onDeleteClicked(it) }
                }
            }
            .show()
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DeleteDialogFragment {
            val fragment = DeleteDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
