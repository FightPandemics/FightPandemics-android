package com.fightpandemics.login.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.utils.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import javax.inject.Inject

class SignInEmailFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory
    //private val loginViewModel: LoginViewModel by viewModels<LoginViewModel> { loginViewModelFactory }
    private lateinit var sign_in_toolbar: MaterialToolbar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_sign_in_email, container, false)

        sign_in_toolbar = rootView.findViewById(R.id.sign_in_toolbar)
        sign_in_toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        return rootView
    }
}