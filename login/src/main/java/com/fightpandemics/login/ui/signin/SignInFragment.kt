package com.fightpandemics.login.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.auth0.android.Auth0
import com.auth0.android.provider.WebAuthProvider
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SignInFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }
    private lateinit var sign_in_toolbar: MaterialToolbar
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
        val rootView = inflater.inflate(R.layout.fragment_sign_in, container, false)

        sign_in_toolbar = rootView.findViewById(R.id.sign_in_toolbar)
        sign_in_toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        rootView.findViewById<MaterialButton>(R.id.btn_sign_in_email).setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signInEmailFragment)
        }
        rootView.findViewById<ConstraintLayout>(R.id.includeGoogle)?.setOnClickListener {
            login(LoginConnection.GOOGLE, it)
        }
        rootView.findViewById<ConstraintLayout>(R.id.includeFacebook)?.setOnClickListener {
            login(LoginConnection.FACEBOOK, it)
        }
        rootView.findViewById<ConstraintLayout>(R.id.includeLinkedin)?.setOnClickListener {
            login(LoginConnection.LINKEDIN, it)
        }
        auth0 = Auth0(resources.getString(R.string.com_auth0_client_id), resources.getString(R.string.com_auth0_domain))
        auth0.isOIDCConformant = true
        return rootView
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
                    { Toast.makeText(context, "Unexpected error, try again later", Toast.LENGTH_LONG) },
                    {

                        //view.findNavController().navigate(R.id.action_signInFragment_to_mainActivity)
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

