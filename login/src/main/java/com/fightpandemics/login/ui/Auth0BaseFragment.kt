package com.fightpandemics.login.ui

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.SecureCredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.BaseCallback
import com.auth0.android.provider.ResponseType
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.UserProfile
import com.fightpandemics.core.data.local.AuthTokenLocalDataSource
import com.fightpandemics.core.utils.ViewModelFactory
import com.fightpandemics.login.R
import javax.inject.Inject


open class Auth0BaseFragment(

) : Fragment() {
    lateinit var auth0: Auth0
    @Inject
    lateinit var authTokenLocalDataSource: AuthTokenLocalDataSource
    val CALLBACK_START_URL = "https://%s/userinfo"
    val CALLBACK_PROFILE_URL = "https://%s/api/v2/"
    val SCHEME = "demo"
    var credentialsManager: SecureCredentialsManager? = null


    fun init() {
        auth0 = Auth0(
            resources.getString(R.string.com_auth0_client_id),
            resources.getString(R.string.com_auth0_domain)
        )
        auth0.isOIDCConformant = true
    }

    fun doSocialLogin(loginConnection: LoginConnection) {
        WebAuthProvider.login(auth0)
            .withConnection(loginConnection.provider)
            .withScheme(SCHEME)
            .withScope("openid profile email")
            .withResponseType(ResponseType.CODE )
            .withState("fight-pandemics")
            .withAudience(
                String.format(CALLBACK_START_URL, getString(R.string.com_auth0_domain))
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
                        auth0 = Auth0(requireContext())
                        auth0.isOIDCConformant = true
                        authTokenLocalDataSource.setToken(it.accessToken)

                        val accessToken = it.accessToken
                        if (accessToken != null) {
                            getProfile(accessToken) {
                                authTokenLocalDataSource.setUserId(it?.appMetadata?.get("mongo_id") as String?)
                                when (loginConnection) {
                                    LoginConnection.LINKEDIN_SIGNUP, LoginConnection.FACEBOOK_SIGNUP, LoginConnection.GOOGLE_SIGNUP -> {
                                        findNavController().navigate(R.id.action_signUpFragment_to_completeProfileFragment)
                                    }
                                    LoginConnection.LINKEDIN_SIGNIN, LoginConnection.FACEBOOK_SIGNIN, LoginConnection.GOOGLE_SIGNIN -> {
                                        goMain()
                                    }
                                }
                            }
                        } else {
                            goMain()
                        }
                    }
                )
            )
    }

    private fun goMain() {
        val PACKAGE_NAME = "com.fightpandemics"
        val intent = Intent().setClassName(
            PACKAGE_NAME,
            "$PACKAGE_NAME.ui.MainActivity"
        )
        startActivity(intent).apply { requireActivity().finish() }
    }

    fun getProfile(
        accessToken: String,
        loginConnection: (user: UserProfile?) -> Unit
    ) {
        var authenticationAPIClient = AuthenticationAPIClient(auth0)
        authenticationAPIClient.userInfo(accessToken)
            .start(object : BaseCallback<UserProfile, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    loginConnection.invoke(null)
                }

                override fun onSuccess(userProfile: UserProfile?) {
                    //TODO what to do with userProfile
                    loginConnection.invoke(userProfile)
                }
            })
    }
}