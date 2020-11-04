package com.fightpandemics.login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.core.utils.ViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SignInFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }
    private lateinit var sign_in_toolbar: MaterialToolbar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_sign_in, container, false)

        sign_in_toolbar = rootView.findViewById(R.id.sign_in_toolbar)
        sign_in_toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        rootView.findViewById<MaterialButton>(R.id.btn_sign_in_email).setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signInEmailFragment)
        }
        return rootView
    }
}