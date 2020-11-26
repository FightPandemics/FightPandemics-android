package com.fightpandemics.login.ui.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.login.ui.LoginViewModel
import com.google.android.material.appbar.MaterialToolbar
import javax.inject.Inject

class SignUpFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }
    private lateinit var sign_up_toolbar: MaterialToolbar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_sign_up, container, false)

        sign_up_toolbar = rootView.findViewById(R.id.sign_up_toolbar)
        sign_up_toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        return rootView
    }
}
