package com.fightpandemics.login.ui.signup

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.login.ui.BaseFragment
import com.fightpandemics.login.ui.LoginViewModel
import kotlinx.android.synthetic.main.fragment_sign_up_email.*
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up_email, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_sigin_instead.setOnClickListener {
        }

        cl_btn_join.setOnClickListener {
            // if (validEmail && validPassword && validRePassword) {
            executeSignUp(
                et_email.text.toString().trim(),
                et_password.text.toString().trim(),
                et_repassword.text.toString().trim()
            )
            //}
        }
    }

    private fun executeSignUp(email: String, password: String, confirmPassword: String) {
        loginViewModel.doSignUP(email, password, confirmPassword)

        cl_btn_join.background.setColorFilter(requireContext().resources.getColor(R.color.color_button_disabled), PorterDuff.Mode.SRC_ATOP);
        loginViewModel.signup.observe(viewLifecycleOwner, { signupResponse ->
            when {
                signupResponse.isError -> {
                    Toast.makeText(
                        requireContext(),
                        "Oops something wrong please try again later: " + signupResponse.error,
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    when {
                        signupResponse.emailVerified -> {
                            displayorHideView(View.VISIBLE, cl_btn_join)
                            Timber.e("Email verification ${signupResponse.emailVerified}")

                            // TODO 9 - Fix this hardcoded string
                            val PACKAGE_NAME = "com.fightpandemics"
                            val intent = Intent().setClassName(
                                PACKAGE_NAME,
                                "$PACKAGE_NAME.ui.MainActivity"
                            )
                            startActivity(intent).apply { requireActivity().finish() }

                        }
                        !signupResponse.emailVerified -> {
                            Toast.makeText(
                                requireContext(),
                                "Please verify your email and login in",
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(R.id.action_signupEmailFragment_to_signinEmailFragment)
                        }
                    }
                }


            }
        })
    }

    private fun checkLayoutSignUpButton() {
//        if (validEmail && validPassword && validRePassword) {
//            val filter: ColorFilter = PorterDuffColorFilter(
//                resources.getColor(R.color.colorPrimary),
//                PorterDuff.Mode.SRC_ATOP
//            )
//            cl_btn_join.background.colorFilter = filter
//        } else {
//            val filter: ColorFilter = PorterDuffColorFilter(
//                resources.getColor(R.color.color_button_disabled),
//                PorterDuff.Mode.SRC_ATOP
//            )
//            cl_btn_join.background.colorFilter = filter
//        }
    }

}
