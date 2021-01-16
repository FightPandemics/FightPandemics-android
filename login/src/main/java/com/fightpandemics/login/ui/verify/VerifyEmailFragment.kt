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
import com.fightpandemics.login.ui.signin.SignInEmailFragment
import com.fightpandemics.login.util.*
import com.github.razir.progressbutton.bindProgressButton
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in_email.*
import kotlinx.android.synthetic.main.sign_in_toolbar.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
class VerifyEmailFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    private val loginViewModel: LoginViewModel by viewModels { loginViewModelFactory }
    private lateinit var verify_email_toolbar: MaterialToolbar
    private lateinit var btn_verify_email: MaterialButton


    private var email: String? = null
    private var password: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_verify_email, container, false)
        val textview : TextView = rootView.findViewById(R.id.appBar_title_login)
        textview.setText("Verify Email")


//        tv_join_now_instead.apply {
//            joinNow(this)
//        }

        //verify_email_toolbar = rootView.findViewById(R.id.verify_email_toolbar)


        btn_verify_email = rootView.findViewById(R.id.btn_verify_email)
        btn_verify_email.setOnClickListener {
            openEmailApp()
        }

//        this.bindProgressButton(btn_sign_in)

        return rootView
    }

    fun openEmailApp(){
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_EMAIL)

        requireActivity().startActivity(intent)

        findNavController().navigate(R.id.nav_sign_in)
        findNavController().navigate(R.id.action_signInFragment_to_signInEmailFragment)

    }


}