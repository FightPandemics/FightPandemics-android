package com.fightpandemics.login.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.login.ui.BaseFragment
import com.fightpandemics.login.ui.LoginViewModel
import kotlinx.android.synthetic.main.fragment_sign_up_email.*
import kotlinx.android.synthetic.main.sign_up_toolbar.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SignUpEmailFragment : BaseFragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup toolbar
        val toolbar = sign_up_toolbar
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        toolbar.title = ""
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        tv_sigin_instead.setOnClickListener {
            findNavController().navigate(R.id.action_signupEmailFragment_to_signinEmailFragment)
        }
        observeSignUP()

        cl_btn_join.setOnClickListener {
            // if (validEmail && validPassword && validRePassword) {
            if (cl_btn_join.isEnabled) {
                cl_btn_join.isEnabled = false
                loginViewModel.doSignUP(
                    et_email.text.toString().trim(),
                    et_password.text.toString().trim(),
                    et_repassword.text.toString().trim()
                )
            }
        }
    }

    private fun observeSignUP() {
        loginViewModel.signup.observe(
            viewLifecycleOwner,
            { signupResponse ->
                cl_btn_join.isEnabled = true
                when {
                    signupResponse.isError -> {
                        Toast.makeText(
                            requireContext(),
                            "Oops something wrong please try again later: " + signupResponse.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        when {
                            signupResponse.emailVerified -> {
                                displayorHideView(View.VISIBLE, cl_btn_join)
                                Timber.e("Email verification ${signupResponse.emailVerified}")

                                val intent = Intent().setClassName(
                                    packageName,
                                    "$packageName.ui.MainActivity"
                                )
                                startActivity(intent).apply { requireActivity().finish() }
                            }
                            !signupResponse.emailVerified -> {
                                findNavController().navigate(R.id.action_signupEmailFragment_to_verifyEmailFragment)
                            }
                        }
                    }
                }
            }
        )
    }

    companion object {
        private const val packageName = "com.fightpandemics"
    }
}
