package com.fightpandemics.login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.fragment_complete_profile.*
import kotlinx.android.synthetic.main.fragment_sign_up_email.tv_sigin_instead
import javax.inject.Inject

class CompleteProfileFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }
    private lateinit var complete_profile_toolbar: MaterialToolbar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_complete_profile, container, false)

        complete_profile_toolbar = rootView.findViewById(R.id.complete_profile_toolbar)
        complete_profile_toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /*
        etFirstName.validate("Please enter a valid name", tilFirstName) { s -> s.isNotEmpty() }

        etLastName.validate("Please enter a valid last name", tilLastName) { s -> s.isNotEmpty() }

        back_arrow.setOnClickListener {
            activity?.onBackPressed()
        }*/
    }
}