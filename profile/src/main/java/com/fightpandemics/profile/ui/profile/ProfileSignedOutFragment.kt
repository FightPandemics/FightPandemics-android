package com.fightpandemics.profile.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.setBooleanPreference
import com.fightpandemics.profile.R
import kotlinx.android.synthetic.main.profile_signed_out_fragment.*
import kotlinx.android.synthetic.main.profile_toolbar.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ProfileSignedOutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_signed_out_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindListeners()
    }

    @ExperimentalCoroutinesApi
    private fun bindListeners() {
        overview.visibility = View.INVISIBLE
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    findNavController().navigate(com.fightpandemics.R.id.action_profileSignedOutFragment_to_indivProfileSettings)
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }

        logInBtnWhite.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_profileSignedOutFragment_to_signInFragment)
            setBooleanPreference(activity?.applicationContext, "isUserSignedInFromProfile", true)
        }
        signUpBtnBlue.setOnClickListener {
            findNavController().navigate(com.fightpandemics.R.id.action_profileSignedOutFragment_to_signUpFragment)
            setBooleanPreference(activity?.applicationContext, "isUserSignedUpFromProfile", true)
        }
    }
}