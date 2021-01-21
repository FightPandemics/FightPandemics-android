package com.fightpandemics.login.ui

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.BaseCallback
import com.auth0.android.management.ManagementException
import com.auth0.android.management.UsersAPIClient
import com.auth0.android.provider.ResponseType
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.UserProfile
import com.fightpandemics.core.data.local.AuthTokenLocalDataSource
import com.fightpandemics.login.R
import javax.inject.Inject

open class Auth0BaseFragment : Fragment() {
    private lateinit var auth0: Auth0

    @Inject
    lateinit var authTokenLocalDataSource: AuthTokenLocalDataSource

    fun init() {
        auth0 = Auth0(
            resources.getString(R.string.com_auth0_client_id),
            resources.getString(R.string.com_auth0_domain)
        )
        auth0.isOIDCConformant = true
    }

    fun doSocialLogin(loginConnection: LoginConnection) = WebAuthProvider.login(auth0)
        .withConnection(loginConnection.provider)
        .withScheme(SCHEME)
        .withScope(SCOPE)
        .withResponseType(ResponseType.CODE)
        .withState(STATE)
        .withAudience(String.format(CALLBACK_PROFILE_URL, getString(R.string.com_auth0_domain)))
        .start(
            requireActivity(),
            Auth0CallBack(
                {
                    Toast.makeText(
                        context,
                        getString(R.string.unexpected_error_login),
                        Toast.LENGTH_LONG
                    ).show()
                },
                { credentials ->
                    auth0 = Auth0(requireContext())
                    auth0.isOIDCConformant = true
                    val accessToken = credentials.accessToken
                    if (accessToken != null) {
                        getProfile(accessToken) { userProfile ->
                            authTokenLocalDataSource.setUserId(
                                userProfile?.appMetadata?.get(
                                    MONGO_DB_ID
                                ) as String?
                            )
                            authTokenLocalDataSource.setToken(credentials.accessToken)
                            when (loginConnection) {
                                LoginConnection.LINKEDIN_SIGNUP,
                                LoginConnection.FACEBOOK_SIGNUP,
                                LoginConnection.GOOGLE_SIGNUP -> {
                                    findNavController().navigate(R.id.action_signUpFragment_to_completeProfileFragment)
                                }
                                LoginConnection.LINKEDIN_SIGNIN,
                                LoginConnection.FACEBOOK_SIGNIN,
                                LoginConnection.GOOGLE_SIGNIN -> {
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

    private fun goMain() {
        val intent = Intent().setClassName(
            PACKAGE_NAME,
            "$PACKAGE_NAME.ui.MainActivity"
        )
        startActivity(intent).apply { requireActivity().finish() }
    }

    private fun getProfile(
        accessToken: String,
        loginConnection: (user: UserProfile?) -> Unit
    ) {
        val authenticationAPIClient = AuthenticationAPIClient(auth0)
        val usersClient = UsersAPIClient(auth0, accessToken)
        authenticationAPIClient.userInfo(accessToken)
            .start(
                object : BaseCallback<UserProfile?,
                    AuthenticationException?> {
                    override fun onSuccess(payload: UserProfile?) {
                        payload?.id?.let {
                            usersClient.getProfile(it)
                                .start(
                                    object : BaseCallback<UserProfile?,
                                        ManagementException?> {
                                        override fun onSuccess(profile: UserProfile?) {
                                            loginConnection.invoke(profile)
                                        }

                                        override fun onFailure(error: ManagementException) {
                                            print(error) // TODO error login
                                        }
                                    }
                                )
                        }
                    }

                    override fun onFailure(error: AuthenticationException) {
                        print(error) // TODO error login
                    }
                }
            )
    }

    companion object {
        private const val CALLBACK_PROFILE_URL = "https://%s/api/v2/"
        private const val SCHEME = "demo"
        private const val SCOPE =
            "openid profile email offline_access read:current_user update:current_user_metadata"
        private const val STATE = "fight-pandemics"
        private const val MONGO_DB_ID = "mongo_id"
        private const val PACKAGE_NAME = "com.fightpandemics"
    }
}
