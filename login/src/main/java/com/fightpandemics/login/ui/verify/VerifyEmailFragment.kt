package com.fightpandemics.login.ui.verify

import android.content.Context
import android.content.Intent
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
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
class VerifyEmailFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    @FlowPreview
    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }
    private lateinit var verifyEmailToolbar: MaterialToolbar
    private lateinit var btnVerifyEmail: MaterialButton

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
        val rootView = inflater.inflate(R.layout.fragment_verify_email, container, false)
        val textView: TextView = rootView.findViewById(R.id.appBar_title_login)
        textView.text = getString(R.string.go_sign_in)
        btnVerifyEmail = rootView.findViewById(R.id.btn_verify_email)
        btnVerifyEmail.setOnClickListener {
            openEmailApp()
        }
        return rootView
    }

    private fun openEmailApp() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_EMAIL)
        requireActivity().startActivity(intent)
        findNavController().navigate(R.id.nav_sign_in)
        findNavController().navigate(R.id.action_signInFragment_to_signInEmailFragment)
    }
}
