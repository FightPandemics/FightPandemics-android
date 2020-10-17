package com.fightpandemics.profile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fightpandemics.profile.R
import com.fightpandemics.profile.dagger.inject
import com.fightpandemics.profile.databinding.FeedbackFragmentBinding
import com.fightpandemics.utils.ViewModelFactory
import kotlinx.android.synthetic.main.feedback_fragment.view.*
import kotlinx.android.synthetic.main.profile_toolbar.*
import timber.log.Timber
import javax.inject.Inject
//import com.fightpandemics.profile.databinding

class FeedbackFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var binding: FeedbackFragmentBinding

    companion object {
        fun newInstance() = FeedbackFragment()
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
        binding = FeedbackFragmentBinding.inflate(inflater)
//        binding = inflater.inflate(R.layout.feedback_fragment_test, container, false)
        return binding.root
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
