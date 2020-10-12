package com.fightpandemics.profile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.fightpandemics.utils.ViewModelFactory
import kotlinx.android.synthetic.main.profile_toolbar.*
import timber.log.Timber
import javax.inject.Inject

class ProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.profile_fragment, container, false)
        return inflater.inflate(R.layout.feedback_fragment, container, false)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
//
//        overview.setOnClickListener {
//            Timber.d("Overview")
//        }
//
//        toolbar.setOnMenuItemClickListener {
//
//            when (it.itemId) {
//                R.id.settings -> {
//                    Timber.d("Settings")
//                    true
//                }
//
//                else -> {
//                    super.onOptionsItemSelected(it)
//                }
//            }
//        }
//
//    }

}
