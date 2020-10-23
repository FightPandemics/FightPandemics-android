package com.fightpandemics.login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.login.util.allInputFieldsHaveBeenFilled
import com.fightpandemics.login.util.hideKeyboard
import com.fightpandemics.login.util.inValidEmail
import com.fightpandemics.login.util.joinNow
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_sign_in_email.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SignInEmailFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }
    private lateinit var sign_in_email_toolbar: MaterialToolbar
    private lateinit var btn_sign_in: MaterialButton
    private lateinit var tv_join_now_instead: TextView

    private var email: String? = null
    private var password: String? = null

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

        tv_join_now_instead = rootView.findViewById(R.id.tv_join_now_instead)
        tv_join_now_instead.joinNow(requireActivity())

        sign_in_email_toolbar = rootView.findViewById(R.id.sign_in_email_toolbar)
        sign_in_email_toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        btn_sign_in = rootView.findViewById(R.id.btn_sign_in)
        btn_sign_in.setOnClickListener {
            hideKeyboard(requireActivity())
            validateFieldsAndDoLogin()
        }
        return rootView
    }

    private fun doLogin(email: String, password: String) {
        loginViewModel.doLogin(email, password)
        loginViewModel.login.observe(viewLifecycleOwner, {
            when {
                //it.isLoading -> Timber.e("LOGGED IN ${it.email}")
                it.emailVerified -> Timber.e("LOGGED IN ${it.email}")
                !it.emailVerified -> Timber.e("ERROR ${it.error}")
            }
        })
    }

    private fun validateFieldsAndDoLogin() {
        email = et_email.text.toString().trim()
        password = et_password.text.toString().trim()

        when {
            allInputFieldsHaveBeenFilled(
                email,
                password
            ) && inValidEmail(email).isNullOrEmpty() -> {
                displayErrorMsgs(displayEmail = false, displayPass = false, null, null)
                doLogin(email!!, password!!)
            }
            !allInputFieldsHaveBeenFilled(email, password) -> displayErrorMsgs(
                displayEmail = true,
                displayPass = true,
                displayEmailError = getString(R.string.email_empty),
                displayPassError = getString(R.string.password_empty)
            )
            inValidEmail(email)?.isNotEmpty()!! ->
                displayErrorMsgs(true, false, getString(R.string.email_invalid), null)
        }
    }

    private fun displayErrorMsgs(
        displayEmail: Boolean,
        displayPass: Boolean,
        displayEmailError: String?,
        displayPassError: String?
    ) {
        et_email_layout.isErrorEnabled = displayEmail
        et_email_layout.error = displayEmailError
        et_password_layout.isErrorEnabled = displayPass
        et_password_layout.error = displayPassError
    }
}