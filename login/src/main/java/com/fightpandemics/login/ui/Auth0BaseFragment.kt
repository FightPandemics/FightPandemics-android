package com.fightpandemics.login.ui

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.auth0.android.Auth0
import com.auth0.android.provider.WebAuthProvider
import com.fightpandemics.login.R

open class Auth0BaseFragment : Fragment(){
    lateinit var auth0: Auth0
    val CALLBACK_START_URL = "https://%s/userinfo"
    val SCOPE = "demo"

    fun init(){
        auth0 = Auth0(
            resources.getString(R.string.com_auth0_client_id),
            resources.getString(R.string.com_auth0_domain)
        )
        auth0.isOIDCConformant = true
    }

    fun doSocialLogin(loginConnection: LoginConnection) {
        WebAuthProvider.login(auth0)
            .withConnection(loginConnection.provider)
            .withScheme(SCOPE)
            .withScope("openid offline_access")
            .withAudience(
                String.format(CALLBACK_START_URL, getString(R.string.com_auth0_domain))
            )
            .start(
                requireActivity(),5
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