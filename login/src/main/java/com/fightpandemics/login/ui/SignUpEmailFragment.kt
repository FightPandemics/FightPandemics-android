package com.fightpandemics.login.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.login.R
import com.fightpandemics.utils.*
import kotlinx.android.synthetic.main.fragment_sign_up_email.*
import javax.inject.Inject


class SignUpEmailFragment : Fragment(), ICheckLayoutEditText {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory

    private lateinit var viewModel: LoginViewModel

    private var validEmail : Boolean = false
    private var validPassword : Boolean = false
    private var validRePassword : Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Obtaining the login graph from LoginActivity and instantiate
        // the @Inject fields with objects from the graph
        (activity as LoginActivity).loginComponent.inject(this)

        // Now you can access loginViewModel here and onCreateView too
        // (shared instance with the Activity and the other Fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_email, container, false)
    }

    companion object {
        fun newInstance() = SignUpEmailFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        tv_sigin_instead.setOnClickListener{
            (activity as LoginActivity).replaceFragment(SignInFragment.newInstance(), true)
        }

        validEmail = et_email.validate("Please enter a valid email address", tilEmail, this){
               s -> s.isValidEmail()
        }

        validPassword = et_password.validate("Please enter a valid password", tilPassword, this){ s -> s.isValidPassword() }

        validRePassword = et_repassword.validate("Please repeat same password", tilRePassword, this){
                s -> s.isValidRePassword(et_password.getString())
        }
        cl_btn_join.setOnClickListener {
            if(validEmail && validPassword && validRePassword){
                executeSignUP()
            }
        }
    }

    private fun executeSignUP() {

    }

    override fun checkLayout() {
        if(validEmail && validPassword && validRePassword){
            cl_btn_join.setBackgroundColor(resources.getColor(R.color.fightPandemicsNeonBlue));
        }else{
            cl_btn_join.setBackgroundColor(resources.getColor(R.color.color_botton_disabled));
        }
    }
}