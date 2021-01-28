package com.fightpandemics.login.ui.signin

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.login.ui.LoginViewModel
import com.fightpandemics.login.util.*
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_sign_in_email, container, false)

        tv_join_now_instead = rootView.findViewById(R.id.tv_join_now_instead)
        tv_join_now_instead.apply {
            joinNow(this)
        }

        sign_in_email_toolbar = rootView.findViewById(R.id.sign_in_email_toolbar)
        sign_in_email_toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        btn_sign_in = rootView.findViewById(R.id.btn_sign_in)

        this.bindProgressButton(btn_sign_in)
        btn_sign_in.attachTextChangeAnimator() // (Optional) Enable fade In / Fade out animations
        btn_sign_in.setOnClickListener {
            // Show progress with "Loading" text
            btn_sign_in.showProgress {
                buttonTextRes = R.string.signingin
                progressColor = Color.WHITE
            }
            it.apply {
                isEnabled = false
            }
            this.view?.let { dismissKeyboard(it) }
            validateFieldsAndDoLogin()
        }
        return rootView
    }

    private fun doLogin(email: String, password: String) {
        loginViewModel.doLogin(email, password)
        loginViewModel.login.observe(
            viewLifecycleOwner,
            {
                when {
                    it.isLoading -> {
                    }
                    it.emailVerified -> {
                        displayButton(false, R.string.loggedin)
                        Timber.e("LOGGED IN ${it.email}")
                        if (it.user == null) {
                            findNavController().navigate(R.id.action_signInEmailFragment_to_completeProfileFragment)
                        } else {
                            // TODO 9 - Fix this hardcoded string
                            val PACKAGE_NAME = "com.fightpandemics"
                            val intent = Intent().setClassName(
                                PACKAGE_NAME,
                                "$PACKAGE_NAME.ui.MainActivity"
                            )
                            startActivity(intent).apply { requireActivity().finish() }
                        }
                    }
                    !it.emailVerified -> {
                        displayButton(true, R.string.sign_in)
                        Timber.e("ERROR ${it.error}")
                        Snackbar.make(sign_in_email_constraint_layout, "ERROR ${it.error}", Snackbar.LENGTH_LONG).show()
                        // TODO 8 - The user is informed that their credentials are invalid using a Snackbar.
                    }
                }
            }
        )
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
            !allInputFieldsHaveBeenFilled(email, password) -> {
                displayErrorMsgs(
                    displayEmail = true,
                    displayPass = true,
                    displayEmailError = getString(R.string.email_empty),
                    displayPassError = getString(R.string.password_empty)
                )
                displayButton(true, R.string.sign_in)
            }
            inValidEmail(email)?.isNotEmpty()!! -> {
                displayErrorMsgs(true, false, getString(R.string.email_invalid), null)
                displayButton(true, R.string.sign_in)
            }
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

    private fun displayButton(active: Boolean, text: Int) {
        btn_sign_in.hideProgress(text).apply { btn_sign_in.isEnabled = active }
    }
}
