package com.fightpandemics.login.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.auth0.android.Auth0
import com.auth0.android.provider.WebAuthProvider
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.login.databinding.FragmentSignInBinding
import com.fightpandemics.login.ui.Auth0CallBack
import com.fightpandemics.login.ui.LoginConnection
import com.fightpandemics.login.ui.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SignInFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }

    private var fragmentSignInBinding: FragmentSignInBinding? = null

    private lateinit var auth0: Auth0
    private val CALLBACK_START_URL = "https://%s/userinfo"
    private val SCOPE = "demo"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSignInBinding.inflate(inflater, container, false)
        fragmentSignInBinding = binding

        fragmentSignInBinding!!.signInToolbar.signInEmailToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        fragmentSignInBinding!!.btnSignInEmail.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signInEmailFragment)
        }

        fragmentSignInBinding!!.includeGoogle.googleSignin.setOnClickListener {
            login(LoginConnection.GOOGLE, it)
        }

        fragmentSignInBinding!!.includeFacebook.facebookSignin.setOnClickListener {
            login(LoginConnection.FACEBOOK, it)
        }

        fragmentSignInBinding!!.includeLinkedin.linkedInSignin.setOnClickListener {
            login(LoginConnection.LINKEDIN, it)
        }

        auth0 = Auth0(
            resources.getString(R.string.com_auth0_client_id),
            resources.getString(R.string.com_auth0_domain)
        )
        auth0.isOIDCConformant = true
        return binding.root
    }

    override fun onDestroyView() {
        fragmentSignInBinding = null
        super.onDestroyView()
    }

    private fun login(loginConnection: LoginConnection, view: View) {
        WebAuthProvider.login(auth0)
            .withConnection(loginConnection.provider)
            .withScheme(SCOPE)
            .withAudience(
                String.format(
                    CALLBACK_START_URL,
                    getString(R.string.com_auth0_domain)
                )
            )
            .start(
                requireActivity(),
                Auth0CallBack(
                    {
                        Toast.makeText(
                            context,
                            "Unexpected error, try again later",
                            Toast.LENGTH_LONG
                        )
                    },
                    {
                        val PACKAGE_NAME = "com.fightpandemics"
                        val intent = Intent().setClassName(
                            PACKAGE_NAME,
                            "$PACKAGE_NAME.ui.MainActivity"
                        )
                        startActivity(intent).apply { requireActivity().finish() }
                    }
                )
            )
    }


}

