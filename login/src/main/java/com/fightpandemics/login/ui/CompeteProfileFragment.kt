package com.fightpandemics.login.ui

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.login.R
import com.fightpandemics.utils.*
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_complete_profile.*
import kotlinx.android.synthetic.main.fragment_sign_up_email.*
import kotlinx.android.synthetic.main.fragment_sign_up_email.tilEmail
import kotlinx.android.synthetic.main.fragment_sign_up_email.tilPassword
import kotlinx.android.synthetic.main.fragment_sign_up_email.tv_sigin_instead
import kotlinx.android.synthetic.main.sign_up_toolbar.*
import javax.inject.Inject


class CompeteProfileFragment : Fragment() {

    companion object {
        fun newInstance() = CompeteProfileFragment()
    }

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    private lateinit var viewModel: LoginViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Obtaining the login graph from LoginActivity and instantiate
        // the @Inject fields with objects from the graph
        (activity as LoginActivity).loginComponent.inject(this)

        // Now you can access loginViewModel here and onCreateView too
        // (shared instance with the Activity and the other Fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complete_profile, container, false)
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        tv_sigin_instead.setOnClickListener {
            (activity as LoginActivity).replaceFragment(SignInFragment.newInstance(), true)
        }

        etFirstName.validate("Please enter a valid name", tilFirstName) { s -> s.isNotEmpty() }

        etLastName.validate("Please enter a valid last name", tilLastName) { s -> s.isNotEmpty() }

        back_arrow.setOnClickListener {
            activity?.onBackPressed()
        }



    }


}