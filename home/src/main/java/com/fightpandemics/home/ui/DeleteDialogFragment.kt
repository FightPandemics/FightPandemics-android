package com.fightpandemics.home.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.fightpandemics.core.data.model.posts.Post
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.home.R
import com.fightpandemics.home.dagger.inject
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/*
* created by Osaigbovo Odiase
* */
// Dialog for Delete action
@ExperimentalCoroutinesApi
class DeleteDialogFragment : DialogFragment() {

    private lateinit var listener : HomeEventListener
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun getDialog(): Dialog? {

        return MaterialAlertDialogBuilder(requireContext(), R.style.PostMaterialDialog)
            .setTitle(R.string.delete_confirm_alert_title)
            .setMessage(R.string.delete_confirm_alert_msg)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Delete") { _, _ ->
                requireArguments()
                    .getParcelable<Post>("post")?.let { listener.onDeleteClicked(it) }
            }
            .show()
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle, listener : HomeEventListener): DeleteDialogFragment {
            val fragment = DeleteDialogFragment()
            fragment.arguments = bundle
            fragment.listener = listener
            return fragment
        }
    }
}
