package com.fightpandemics.login.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.login.R
import com.fightpandemics.login.makeInvisible
import com.fightpandemics.login.makeVisible
import com.fightpandemics.ui.BaseActivity
import com.fightpandemics.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_sign_in.*
import javax.inject.Inject


class SignInFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    private lateinit var viewModel: LoginViewModel
    private var email: String? = null
    private var password: String? = null

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
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout_btn_email.setOnClickListener {
            navigateToSignInWithEmail()
        }

        layout_btn_sign_in.setOnClickListener {
            validateFieldsAndDoLogin()
        }

        tv_join_now_instead.setOnClickListener {
            navigateToSignUp()
        }
    }


    private fun navigateToSignInWithEmail() {
        layout_btn_google.makeInvisible()
        layout_btn_facebook.makeInvisible()
        layout_btn_linkedin.makeInvisible()
        tv_privacy_policy.makeInvisible()
        layout_btn_email.makeInvisible()
        etFirstName.makeVisible()
        etLastName.makeVisible()
        layout_btn_sign_in.makeVisible()
        tv_forgot_password.makeVisible()
        tv_join_now_instead.makeVisible()
    }

    private fun doLogin(email: String, password: String) {
        viewModel.doLogin(email, password)
    }

    private fun validateFieldsAndDoLogin() {
        when {
            viewModel.allInputFieldsHaveBeenFilled(etFirstName.text.toString().trim(), etLastName.text.toString().trim())
                    && viewModel.inValidEmail(etFirstName.text.toString().trim()).isNullOrEmpty()
            -> {
                email = etFirstName.text.toString().trim()
                password = etLastName.text.toString().trim()
                doLogin(email!!, password!!)
            }
            !viewModel.allInputFieldsHaveBeenFilled(etFirstName.text.toString().trim(), etLastName.text.toString().trim())
            -> {}//Display error messages
            viewModel.inValidEmail(etFirstName.text.toString().trim())!!.isNotEmpty()
            -> {}//Display "Please Enter a Valid Email Address"
        }
    }

    private fun navigateToSignUp() {
        (activity as BaseActivity).replaceFragment(SignUpFragment.newInstance(), true)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignInFragment()
    }
}