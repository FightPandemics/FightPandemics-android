package com.fightpandemics.login.ui.signin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.login.databinding.FragmentSignInBinding
import com.fightpandemics.login.ui.LoginConnection
import com.fightpandemics.login.ui.LoginViewModel
import com.fightpandemics.login.ui.Auth0BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SignInFragment : Auth0BaseFragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }

    private var fragmentSignInBinding: FragmentSignInBinding? = null


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
        super.init()
        val binding = FragmentSignInBinding.inflate(inflater, container, false)
        fragmentSignInBinding = binding

        fragmentSignInBinding!!.signInToolbar.signInEmailToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        fragmentSignInBinding!!.btnSignInEmail.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signInEmailFragment)
        }

        fragmentSignInBinding!!.includeGoogle.googleSignin.setOnClickListener {
            doSocialLogin(LoginConnection.GOOGLE_SIGNIN)
        }

        fragmentSignInBinding!!.includeFacebook.facebookSignin.setOnClickListener {
            doSocialLogin(LoginConnection.FACEBOOK_SIGNIN)
        }

        fragmentSignInBinding!!.includeLinkedin.linkedInSignin.setOnClickListener {
            doSocialLogin(LoginConnection.LINKEDIN_SIGNIN)
        }
        return binding.root
    }

    override fun onDestroyView() {
        fragmentSignInBinding = null
        super.onDestroyView()
    }

}
