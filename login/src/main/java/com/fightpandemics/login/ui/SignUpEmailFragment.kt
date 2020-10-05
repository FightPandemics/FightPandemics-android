package com.fightpandemics.login.ui

import android.content.Context
import android.graphics.Color
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
import kotlinx.android.synthetic.main.fragment_sign_up_email.*
import javax.inject.Inject


class SignUpEmailFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    private lateinit var viewModel: LoginViewModel

    private var validEmail: Boolean = false
    private var validPassword: Boolean = false
    private var validRePassword: Boolean = false

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
        return inflater.inflate(R.layout.fragment_sign_up_email, container, false)
    }

    companion object {
        fun newInstance() = SignUpEmailFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        tv_sigin_instead.setOnClickListener {
            (activity as LoginActivity).replaceFragment(SignInFragment.newInstance(), true)
        }

        et_email.validateET("Please enter a valid email address", tilEmail) { s -> s.isValidEmail() }

        et_password.validateET("Please enter a valid password", tilPassword) { s -> s.isValidPassword() }

        et_repassword.validateET("Please repeat same password", tilRePassword) { s ->s.isValidRePassword(
            et_password.getString()
        )}

        cl_btn_join.setOnClickListener {
            if (validEmail && validPassword && validRePassword) {
                executeSignUP()
            }
        }

    }

    private fun executeSignUP() {

    }

    private fun EditText.validateET(
        message: String,
        textInputLayout: TextInputLayout,
        validator: (String) -> Boolean
    ): Boolean {
        this.afterTextChanged {
            val isValid = validator(it)
            if (!isValid) {
                textInputLayout.isErrorEnabled = true
                textInputLayout.error = message
            } else {
                textInputLayout.isErrorEnabled = false
            }

            checkValidations(isValid, textInputLayout.id)
        }

        return validator(this.getString())
    }

    private fun checkValidations(boolean: Boolean, id: Int) {
        when (id) {
            tilEmail.id -> validEmail = boolean
            tilPassword.id -> {
                validPassword = boolean
                if(!et_repassword.isEmpty())
                et_repassword.text = et_repassword.text
            }
            tilRePassword.id -> validRePassword = boolean
        }
        checkLayoutSignUPButton()
    }

    private fun checkLayoutSignUPButton() {
        if (validEmail && validPassword && validRePassword) {
            val filter: ColorFilter = PorterDuffColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
            cl_btn_join.background.colorFilter = filter
        } else {
            val filter: ColorFilter = PorterDuffColorFilter(resources.getColor(R.color.color_button_disabled), PorterDuff.Mode.SRC_ATOP)
            cl_btn_join.background.colorFilter = filter
        }
    }
}