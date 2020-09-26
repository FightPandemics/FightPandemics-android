package com.fightpandemics.login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.utils.ViewModelFactory
import javax.inject.Inject

class SignInFragment : Fragment() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelFactory
    private lateinit var viewModel: LoginViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }
}