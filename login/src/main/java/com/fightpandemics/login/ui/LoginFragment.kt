package com.fightpandemics.login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.utils.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class LoginFragment : Fragment() {

    private lateinit var homePager: ViewPager

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: LoginViewModel

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val rootView = inflater.inflate(R.layout.activity_login, container, false)

        homePager = rootView.findViewById(R.id.homePager)

        setupUi()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private fun setupUi() {

    }
}
