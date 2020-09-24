package com.fightpandemics.login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.login.R
import com.fightpandemics.login.dagger.inject
import com.fightpandemics.utils.ViewModelFactory
import kotlinx.android.synthetic.main.login_toolbar.*
import timber.log.Timber
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        overview.setOnClickListener {
            Timber.d("Overview")
        }

        toolbar.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.settings -> {
                    Timber.d("Settings")
                    true
                }

                else -> {
                    super.onOptionsItemSelected(it)
                }
            }
        }

    }
}
