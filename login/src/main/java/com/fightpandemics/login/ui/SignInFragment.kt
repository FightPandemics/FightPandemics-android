package com.fightpandemics.login.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fightpandemics.login.R
import com.fightpandemics.login.util.makeInvisible
import com.fightpandemics.login.util.makeVisible
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
        et_email.makeVisible()
        et_password.makeVisible()
        layout_btn_sign_in.makeVisible()
        tv_forgot_password.makeVisible()
        tv_join_now_instead.makeVisible()
    }

    private fun doLogin(email: String, password: String) {
        viewModel.doLogin(email, password)
    }

    private fun validateFieldsAndDoLogin() {
        when {
            viewModel.allInputFieldsHaveBeenFilled(et_email.text.toString().trim(), et_password.text.toString().trim())
                    && viewModel.inValidEmail(et_email.text.toString().trim()).isNullOrEmpty()
            -> {
                email = et_email.text.toString().trim()
                password = et_password.text.toString().trim()
                doLogin(email!!, password!!)
            }
            !viewModel.allInputFieldsHaveBeenFilled(et_email.text.toString().trim(), et_password.text.toString().trim())
            -> {
                et_email_layout.isErrorEnabled = true
                et_email_layout.error = "Email cannot be Empty"
                et_password_layout.isErrorEnabled = true
                et_password_layout.error = "Password cannot be Empty"
            }
            viewModel.inValidEmail(et_email.text.toString().trim())!!.isNotEmpty()
            -> {
                et_email_layout.isErrorEnabled = true
                et_email_layout.error = "Please Enter a Valid Email Address"
            }
        }
    }

    private fun navigateToSignUp() {
        (activity as LoginActivity).replaceFragment(SignUpFragment.newInstance(), true)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignInFragment()
    }
}