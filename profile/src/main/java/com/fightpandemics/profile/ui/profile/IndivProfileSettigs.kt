package com.fightpandemics.profile.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.filter.dagger.inject
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import kotlinx.android.synthetic.main.activity_logged_in.*
import kotlinx.android.synthetic.main.profile_fragment_content.*
import kotlinx.android.synthetic.main.profile_toolbar.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject
import kotlinx.android.synthetic.main.profile_toolbar.toolbar as toolbar1

class IndivProfileSettings: Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @ExperimentalCoroutinesApi
    private val profileViewModel: ProfileViewModel by activityViewModels { viewModelFactory }

    companion object {
        fun newInstance() = IndivProfileSettings()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }
    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        bindListeners()
//        profileViewModel.getIndividualProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_logged_in, container, false)
    }

    private fun bindListeners() {
//        toolbar.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.settings -> {
//                    Timber.d("bindListeners: Settings")
//                    findNavController().navigate(com.fightpandemics.R.id.action_profileFragment_to_indivProfileSettings)
//                    true
//                }
//
//                else -> {
//                    super.onOptionsItemSelected(it)
//                }
//            }
//        }

//        button3?.setOnClickListener {
//            findNavController().navigate(com.fightpandemics.R.id.action_profileFragment_to_editProfileFragment)
//        }
//
//        profileViewModel.individualProfile.observe(viewLifecycleOwner) { profile ->
//            getIndividualProfileListener(profile)
//        }

        updateAccountInfoContainer.setOnClickListener{
            findNavController().navigate(com.fightpandemics.R.id.action_indivProfileSettings_to_editAccountFragment)
        }
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}